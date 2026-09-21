package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.entities.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginRepositoryImp implements LoginRepository{
    private static final Logger logger = LoggerFactory.getLogger(LoginRepositoryImp.class);

    private final JdbcClient jdbcClient;

    public LoginRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Login> findByEmail(String email) {
        logger.info("LoginRepository -> findByEmail");
        return this.jdbcClient
                .sql("SELECT * FROM LOGIN WHERE EMAIL = :email")
                .param("email", email)
                .query(Login.class)
                .optional();
    }

    @Override
    public Integer save(Login login) {
        logger.info("LoginRepository -> save");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var linhas = this.jdbcClient
                .sql("INSERT INTO LOGIN (EMAIL, SENHA, ULTIMA_TROCA) VALUES (:email,:senha, :ultimaManutencao)")
                .param("email", login.getEmail())
                .param("senha", login.getSenha())
                .param("ultimaManutencao", java.time.LocalDateTime.now())
                .update(keyHolder, "ID_USUARIO");
        Number key = keyHolder.getKey();
        if (key != null) {
            login.setIdUsuario(key.longValue());
        }
        return linhas;
    }

    @Override
    public Integer update(Login login) {
        logger.info("LoginRepository -> update");
        return this.jdbcClient
                .sql("UPDATE LOGIN SET EMAIL = :email, SENHA = :senha, ULTIMA_TROCA = :ultimaManutencao WHERE ID_USUARIO = :id")
                .param("id", login.getIdUsuario())
                .param("email", login.getEmail())
                .param("senha", login.getSenha())
                .param("ultimaManutencao", java.time.LocalDateTime.now())
                .update();
    }
    @Override
    public Integer delete(Long id) {
        logger.info("LoginRepository -> delete");
        return this.jdbcClient
                .sql("DELETE FROM LOGIN WHERE ID_USUARIO = :id")
                .param("id", id)
                .update();
    }

    @Override
    public Optional<Login> findLoginById(Long idUsuario) {
        logger.info("LoginRepository -> findLoginById");
        return this.jdbcClient
                .sql("SELECT * FROM LOGIN WHERE ID_USUARIO = :idUsuario")
                .param("idUsuario", idUsuario)
                .query(Login.class)
                .optional();
       }
}
