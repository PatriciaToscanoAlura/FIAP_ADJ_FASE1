package br.com.fiap.TodosRestaurantes.services;

import br.com.fiap.TodosRestaurantes.dtos.IncluiUsuarioRequestDTO;
import br.com.fiap.TodosRestaurantes.dtos.TrocaSenhaDTO;
import br.com.fiap.TodosRestaurantes.dtos.ValidaLoginDTO;
import br.com.fiap.TodosRestaurantes.entities.Login;
import br.com.fiap.TodosRestaurantes.repositories.LoginRepository;
import br.com.fiap.TodosRestaurantes.services.exceptions.LoginIncorreto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Login validaLogin(ValidaLoginDTO loginDTO) {
        logger.info("LoginService -> validaLogin");
        var loginEntity = obtemDadosLogin(loginDTO);
        if (loginEntity.isEmpty()) throw new LoginIncorreto("Login não encontrado");
        if(passwordEncoder.matches(loginDTO.login().senha(), loginEntity.get().getSenha())) {
            return loginEntity.get();
       } else throw new LoginIncorreto("Login com senha inválida");
    }

    public boolean confirmaLogin(TrocaSenhaDTO trocaSenhaDTO, Login usuarioLogado) {
        logger.info("LoginService -> confirmaLogin");
        var email = trocaSenhaDTO.loginAtual().email();
        var senha = trocaSenhaDTO.loginAtual().senha();
        return passwordEncoder.matches(senha, usuarioLogado.getSenha()) && email.equals(usuarioLogado.getEmail());
    }

    public Login saveLogin(IncluiUsuarioRequestDTO usuario) {
        logger.info("LoginService -> saveLogin");
        var loginEntity = obtemDadosLogin(usuario);
        assert this.loginRepository != null;
        this.loginRepository.save(loginEntity);
        return loginEntity;
    }

    public Optional<Login> findByEmail(String email){
        logger.info("LoginService -> findByEmail");
        return this.loginRepository.findByEmail(email);
    }

    public String emailById(Long idUsuario) {
        logger.info("LoginService -> emailById");
        var login = this.loginRepository.findLoginById(idUsuario).get();
        return login.getEmail();
    }

    public boolean alteraSenhaLogin(Login usuarioLogado, String novaSenha) {
        logger.info("LoginService -> alteraSenhaLogin");
        usuarioLogado.setSenha(passwordEncoder.encode(novaSenha));
        return this.loginRepository.update(usuarioLogado) == 1;
    }

    public void alteraEmailLogin(Login usuarioLogado, String email) {
        logger.info("LoginService -> alteraEmailLogin");
        usuarioLogado.setEmail(email);
        this.loginRepository.update(usuarioLogado);
    }

    private Optional<Login> obtemDadosLogin(ValidaLoginDTO loginDTO) {
        logger.info("LoginService -> obtemDadosLogin - validação");
        var email = loginDTO.login().email();
        return this.loginRepository.findByEmail(email);
    }

    private Login obtemDadosLogin(IncluiUsuarioRequestDTO usuario) {
        logger.info("LoginService -> obtemDadosLogin - inclusão");
        var email = usuario.login().email();
        var senha = passwordEncoder.encode(usuario.login().senha());
        return new Login(email, senha);
    }

}
