package br.com.fiap.TodosRestaurantes.controllers;

import br.com.fiap.TodosRestaurantes.config.ControleVersionamentoApi;
import br.com.fiap.TodosRestaurantes.dtos.ResponseLoginDTO;
import br.com.fiap.TodosRestaurantes.dtos.ValidaLoginDTO;
import br.com.fiap.TodosRestaurantes.entities.DonoRestaurante;
import br.com.fiap.TodosRestaurantes.model.TipoUsuario;
import br.com.fiap.TodosRestaurantes.services.JwtService;
import br.com.fiap.TodosRestaurantes.services.LoginService;
import br.com.fiap.TodosRestaurantes.services.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@ControleVersionamentoApi("1")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public LoginController(LoginService loginService, JwtService jwtService, UsuarioService usuarioService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ResponseLoginDTO> validaLogin(
            @Valid @RequestBody ValidaLoginDTO login
    ) {
        logger.info("POST -> /login - verão 1");
        var usuarioLogin = loginService.validaLogin(login);
        var jwt = jwtService.geraToken(usuarioLogin.getIdUsuario(), usuarioLogin.getEmail(), usuarioLogin.getUltimaTroca());
        var usuario = this.usuarioService.findUsuarioById(usuarioLogin.getIdUsuario());
        var tipoUsuario = TipoUsuario.CLIENTE_RESTAURANTE;
        if (usuario instanceof DonoRestaurante) {
            tipoUsuario = TipoUsuario.DONO_RESTAURANTE;
        }
        var responseValidaLogin = new ResponseLoginDTO(3600, jwt, usuarioLogin.getIdUsuario(), tipoUsuario.name());
        return ResponseEntity.ok(responseValidaLogin);
    }

}
