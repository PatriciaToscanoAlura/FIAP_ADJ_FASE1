package br.com.fiap.TodosRestaurantes.config;

import br.com.fiap.TodosRestaurantes.entities.Login;
import br.com.fiap.TodosRestaurantes.services.JwtService;
import br.com.fiap.TodosRestaurantes.services.exceptions.RecursoNaoEncontrado;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class FiltroAutenticacao extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(FiltroAutenticacao.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public FiltroAutenticacao(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Verifica se o token veio no formato padrão: "Bearer <TOKEN>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.replace("Bearer ", "").trim(); // Remove a palavra "Bearer " e pega só o JWT
        userEmail = jwtService.extrairEmail(jwt); // Extrai o e-mail

        logger.info("Validação Login - email:" + userEmail);
        logger.info("Validação token - token:" + jwt);
        // Se achou o e-mail e o usuário ainda não está autenticado no contexto atual
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                LocalDateTime ultimaAtualizacao = new Timestamp(0).toLocalDateTime();
                if (userDetails instanceof Login usuarioLogado) {
                    ultimaAtualizacao = usuarioLogado.getUltimaTroca();
                    logger.info("Ultima atualizacao Login: " + ultimaAtualizacao);
                }

                // Se o token for legítimo e não expirou, autentica o usuário no Spring
                if (jwtService.isTokenValido(jwt, userDetails.getUsername(), ultimaAtualizacao)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Salva o usuário na sessão interna da requisição do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("Erro na autenticação, tente novamente");
            }
            // Continua o fluxo da requisição para o endpoint
            filterChain.doFilter(request, response);
    }   }
}
