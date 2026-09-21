package br.com.fiap.TodosRestaurantes.services;

import br.com.fiap.TodosRestaurantes.repositories.LoginRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${api.security.token.secret}")
    private String secretKey;

    private final LoginRepository loginRepository;

    public JwtService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    private SecretKey getChaveAssinatura() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String geraToken(Long idUsuario, String email, LocalDateTime loginAtual) {
        logger.info("JwtService -> geraToken");
        var agora = new Date();
        int horaMilisegundos = 3600000;
        var expiracao = new Date(agora.getTime() + horaMilisegundos);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", idUsuario);
        extraClaims.put("ultimaAtualizacao", loginAtual.toString());
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(expiracao).expiration(expiracao)
                .signWith(getChaveAssinatura())
                .compact();
    }

    public boolean isTokenValido(String token, String emailUsuarioBanco, LocalDateTime ultManutBanco) {
        final String emailToken = extrairEmail(token);
        final LocalDateTime atualizacaoToken = extrairUltimaAlteracao(token);
        return (emailToken.equals(emailUsuarioBanco) && !isTokenExpirado(token) && atualizacaoToken.equals(ultManutBanco));
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    private LocalDateTime extrairUltimaAlteracao(String token) {
        String ultimaAlteracao = extrairClaim(token, claims -> claims.get("ultimaAtualizacao", String.class));
        if (ultimaAlteracao != null) {
            return LocalDateTime.parse(ultimaAlteracao);
        }
        return new java.sql.Timestamp(0).toLocalDateTime();
    }

    private <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getChaveAssinatura())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpirado(String token) {
        Date expiracao = extrairClaim(token, Claims::getExpiration);
        return expiracao.before(new Date());
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.loginRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Erro na autenticação, tente novamente"));
    }
}

