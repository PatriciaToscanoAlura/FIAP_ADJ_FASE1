package br.com.fiap.TodosRestaurantes.repositories;

import br.com.fiap.TodosRestaurantes.entities.Endereco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EnderecoRepositoryImp implements EnderecoRepository{
    private static final Logger logger = LoggerFactory.getLogger(EnderecoRepositoryImp.class);
    private final JdbcClient jdbcClient;

    public EnderecoRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Endereco> findById(Long id) {
        logger.info("EnderecoRepository -> findById");
        return this.jdbcClient
                .sql("SELECT * FROM ENDERECO WHERE ID_ENDERECO = :id")
                .param("id", id)
                .query(Endereco.class)
                .optional();
    }

    @Override
    public Integer save(Endereco endereco) {
        logger.info("EnderecoRepository -> save");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var linhas = this.jdbcClient
                .sql("INSERT INTO ENDERECO (TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO, BAIRRO, CEP, CIDADE, UF) VALUES (:tpoLogradouro, :nomeLogradouro, :numero, :bairro, :cep, :cidade, :uf)")
                .param("tpoLogradouro", endereco.getTipoLogradouro())
                .param("nomeLogradouro", endereco.getNomeLogradouro())
                .param("numero",endereco.getNumero())
                .param("bairro", endereco.getBairro())
                .param("cep", endereco.getCep())
                .param("cidade", endereco.getCidade())
                .param("uf", endereco.getUf())
                .update(keyHolder, "ID_ENDERECO");
        Number key = keyHolder.getKey();
        if (key != null) {
            endereco.setIdEndereco(key.longValue());
        }
        return linhas;
    }

    @Override
    public Integer update(Endereco endereco) {
        logger.info("EnderecoRepository -> update");
        return this.jdbcClient
                .sql("UPDATE ENDERECO SET ENDERECO.TIPO_LOGRADOURO = :tpoLogradouro, NOME_LOGRADOURO = :nomeLogradouro, NUMERO = :numero, BAIRRO = :bairro, CEP = :cep, CIDADE = :cidade, UF = :uf WHERE ID_ENDERECO = :id")
                .param("id", endereco.getIdEndereco())
                .param("tpoLogradouro", endereco.getTipoLogradouro())
                .param("nomeLogradouro", endereco.getNomeLogradouro())
                .param("numero",endereco.getNumero())
                .param("bairro", endereco.getBairro())
                .param("cep", endereco.getCep())
                .param("cidade", endereco.getCidade())
                .param("uf", endereco.getUf())
                .update();
    }

    @Override
    public Integer delete(Endereco endereco) {
        logger.info("EnderecoRepository -> delete");
        return this.jdbcClient
                .sql("DELETE FROM ENDERECO WHERE ID_ENDERECO = :id")
                .param("id", endereco.getIdEndereco())
                .update();
    }
}
