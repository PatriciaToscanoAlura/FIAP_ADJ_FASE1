package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.dtos.LinhaDonoRestauranteDTO;
import br.com.fiap.TodosRestaurantes.entities.DonoRestaurante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class DonoRestauranteRepositoryImp implements DonoRestauranteRepository{
    private static final Logger logger = LoggerFactory.getLogger(DonoRestauranteRepositoryImp.class);

    private final JdbcClient jdbcClient;

    public DonoRestauranteRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<LinhaDonoRestauranteDTO> findById(Long id) {
        logger.info("DonoRestauranteRepository -> findById");
        return this.jdbcClient
                .sql("SELECT * FROM DONORESTAURANTE WHERE ID_USUARIO = :id")
                .param("id", id)
                .query(LinhaDonoRestauranteDTO.class)
                .optional();
    }

    @Override
    public Integer save(DonoRestaurante donoRestaurante) {
        logger.info("DonoRestauranteRepository -> save");
        return this.jdbcClient
                .sql("INSERT INTO DONORESTAURANTE (ID_USUARIO, NOME, ID_ENDERECO, ULTIMA_ALTERACAO) VALUES (:id,:nome, :endereco, :ultimaAlteracao)")
                .param("id", donoRestaurante.getIdUsuario())
                .param("nome", donoRestaurante.getNome())
                .param("endereco", donoRestaurante.getEndereco().getIdEndereco())
                .param("ultimaAlteracao", java.time.LocalDateTime.now())
                .update();
    }

    @Override
    public Integer update(DonoRestaurante donoRestaurante) {
        logger.info("DonoRestauranteRepository -> update");
        return this.jdbcClient
                .sql("UPDATE DONORESTAURANTE SET NOME = :nome, ID_ENDERECO = :endereco, ULTIMA_ALTERACAO = :ultimaAlteracao WHERE ID_USUARIO = :id")
                .param("id", donoRestaurante.getIdUsuario())
                .param("nome", donoRestaurante.getNome())
                .param("endereco", donoRestaurante.getEndereco().getIdEndereco())
                .param("ultimaAlteracao", java.time.LocalDateTime.now())
                .update();
    }

    @Override
    public Integer delete(DonoRestaurante donoRestaurante) {
        logger.info("DonoRestauranteRepository -> delete");
        return this.jdbcClient
                .sql("DELETE FROM DONORESTAURANTE WHERE ID_USUARIO = :id")
                .param("id", donoRestaurante.getIdUsuario())
                .update();
    }
}
