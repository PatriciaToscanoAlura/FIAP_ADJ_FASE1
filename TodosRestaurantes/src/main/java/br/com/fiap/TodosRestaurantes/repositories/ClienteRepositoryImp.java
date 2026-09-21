package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.dtos.LinhaClienteDTO;
import br.com.fiap.TodosRestaurantes.entities.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryImp implements ClienteRepository{
    private static final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImp.class);

    private final JdbcClient jdbcClient;

    public ClienteRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<LinhaClienteDTO> findById(Long id) {
        logger.info("CienteRepository -> findById");
        return this.jdbcClient
                .sql("SELECT * FROM CLIENTE WHERE ID_USUARIO = :id")
                .param("id", id)
                .query(LinhaClienteDTO.class)
                .optional();
    }

    @Override
    public Integer save(Cliente cliente) {
        logger.info("CienteRepository -> save");
        return this.jdbcClient
                .sql("INSERT INTO CLIENTE (ID_USUARIO, NOME, ID_ENDERECO, ULTIMA_ALTERACAO) VALUES (:id,:nome, :endereco, :ultimaAlteracao)")
                .param("id", cliente.getIdUsuario())
                .param("nome", cliente.getNome())
                .param("endereco", cliente.getEndereco().getIdEndereco())
                .param("ultimaAlteracao", java.time.LocalDateTime.now())
                .update();
    }

    @Override
    public Integer update(Cliente cliente) {
        logger.info("CienteRepository -> update");
        return this.jdbcClient
                .sql("UPDATE CLIENTE SET NOME = :nome, ID_ENDERECO = :endereco, ULTIMA_ALTERACAO = :ultimaAlteracao WHERE ID_USUARIO = :id")
                .param("id", cliente.getIdUsuario())
                .param("nome", cliente.getNome())
                .param("endereco", cliente.getEndereco().getIdEndereco())
                .param("ultimaAlteracao", java.time.LocalDateTime.now())
                .update();
    }
    @Override
    public Integer delete(Cliente cliente) {
        logger.info("CienteRepository -> delete");
        return this.jdbcClient
                .sql("DELETE FROM CLIENTE WHERE ID_USUARIO = :id")
                .param("id", cliente.getIdUsuario())
                .update();
    }

    @Override
    public List<LinhaClienteDTO> findByNome(String nomeCliente) {
        logger.info("CienteRepository -> findByNome");
        return this.jdbcClient
                .sql("SELECT * FROM CLIENTE WHERE NOME LIKE :nome")
                .param("nome", "%" + nomeCliente + "%")
                .query(LinhaClienteDTO.class)
                .list();
    }
}
