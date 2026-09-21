package br.com.fiap.TodosRestaurantes.services;

import br.com.fiap.TodosRestaurantes.dtos.AlteraUsuarioDTO;
import br.com.fiap.TodosRestaurantes.dtos.IncluiUsuarioRequestDTO;
import br.com.fiap.TodosRestaurantes.dtos.LinhaClienteDTO;
import br.com.fiap.TodosRestaurantes.entities.*;
import br.com.fiap.TodosRestaurantes.repositories.ClienteRepository;
import br.com.fiap.TodosRestaurantes.repositories.DonoRestauranteRepository;
import br.com.fiap.TodosRestaurantes.repositories.EnderecoRepository;
import br.com.fiap.TodosRestaurantes.repositories.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
   private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

   private final LoginRepository loginRepository;
   private final ClienteRepository clienteRepository;
   private final DonoRestauranteRepository donoRestauranteRepository;
   private final EnderecoRepository enderecoRepository;


    public UsuarioService(LoginRepository loginRepository, ClienteRepository clienteRepository, DonoRestauranteRepository donoRestauranteRepository, EnderecoRepository enderecoRepository) {
        this.loginRepository = loginRepository;
        this.clienteRepository = clienteRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public void saveUsuario(IncluiUsuarioRequestDTO usuario, Long id) {
        logger.info("UsuarioService -> saveUsuario");
        var tipoUsuario = usuario.tipoUsuario();
        var endereco = obtemEndereco(usuario);
        var nome = usuario.nome();
        if (this.enderecoRepository.save(endereco) > 0) {
        switch (tipoUsuario) {
                case DONO_RESTAURANTE ->  insereDonoRestaurante(id, nome, endereco);
                case CLIENTE_RESTAURANTE -> insereCliente(id, nome, endereco);
            }
        }
    }

    public List<Cliente> findClienteByNome(String nome) {
        logger.info("UsuarioService -> findClienteByNome");
        List<LinhaClienteDTO> listaLinhas = this.clienteRepository.findByNome(nome);
        List<Cliente> listaClientes = new ArrayList<>();
        for (LinhaClienteDTO linha: listaLinhas) {
            var endereco = this.enderecoRepository.findById(linha.idEndereco()).get();
            listaClientes.add(new Cliente(linha.idUsuario(), linha.nome(), endereco, linha.ultimaAlteracao()));
        }
        return listaClientes;
    }

    public Integer alteraUsuarioById(AlteraUsuarioDTO usuarioDTO, Long id) {
        logger.info("UsuarioService -> alterarUsuarioById");
        var usuario = this.findUsuarioById(id);
        var endereco = new Endereco(usuarioDTO.endereco().tipoLogradouro(),
                                    usuarioDTO.endereco().nomeLogradouro(),
                                    usuarioDTO.endereco().numero(),
                                    usuarioDTO.endereco().bairro(),
                                    usuarioDTO.endereco().cidade(),
                                    usuarioDTO.endereco().cepCompleto(),
                                    usuarioDTO.endereco().uf());
        this.enderecoRepository.update(endereco);
        if (usuario instanceof Cliente cliente) {
            endereco.setIdEndereco(cliente.getEndereco().getIdEndereco());
            this.enderecoRepository.update(endereco);
            return this.clienteRepository.update(new Cliente(cliente.getIdUsuario(), usuarioDTO.nome(), endereco));
        }
        if (usuario instanceof DonoRestaurante donoRestaurante) {
            endereco.setIdEndereco(donoRestaurante.getEndereco().getIdEndereco());
            return this.donoRestauranteRepository.update(new DonoRestaurante(donoRestaurante.getIdUsuario(), usuarioDTO.nome(), endereco));
        }
        return 0;
    }

    public Usuario findUsuarioById(Long idUsuario) {
        logger.info("UsuarioService -> findUsuarioById");
        if (this.clienteRepository.findById(idUsuario).isPresent()) {
            var linhaCliente = this.clienteRepository.findById(idUsuario).get();
            var endereco = this.enderecoRepository.findById(linhaCliente.idEndereco()).get();
            return new Cliente(linhaCliente.idUsuario(), linhaCliente.nome(), endereco, linhaCliente.ultimaAlteracao());
        } else {
            var linhaDonoRestaurante = this.donoRestauranteRepository.findById(idUsuario).get();
            var endereco = this.enderecoRepository.findById(linhaDonoRestaurante.idEndereco()).get();
            return new DonoRestaurante(linhaDonoRestaurante.idUsuario(), linhaDonoRestaurante.nome(), endereco, linhaDonoRestaurante.ultimaAlteracao());
        }
    }

    public Endereco enderecoById(Long idUsuario) {
        logger.info("UsuarioService -> enderecoById");
        return this.enderecoRepository.findById(idUsuario).get();
    }

    @Transactional
    public Integer deletaUsuarioById(Long idUsuario) {
        logger.info("UsuarioService -> deletaUsuarioById");
        return this.loginRepository.delete(idUsuario);
    }

    private Endereco obtemEndereco(IncluiUsuarioRequestDTO usuario) {
        logger.info("UsuarioService -> obtemEndereco");
        var tipoLogradouro = usuario.endereco().tipoLogradouro().toString();
        var nomeLogradouro = usuario.endereco().nomeLogradouro();
        var numero = usuario.endereco().numero();
        var bairro = usuario.endereco().bairro();
        var cidade = usuario.endereco().cidade();
        var cep = usuario.endereco().cepCompleto();
        var uf = usuario.endereco().uf().toString();
        return new Endereco(tipoLogradouro, nomeLogradouro, numero, bairro, cidade, cep, uf);
    }

    private void insereDonoRestaurante(Long id, String nome, Endereco endereco) {
        logger.info("UsuarioService -> insereDonoRestaurante");
        var donoRestaurante = new DonoRestaurante(id, nome, endereco);
        this.donoRestauranteRepository.save(donoRestaurante);
    }

    private void insereCliente(Long id, String nome, Endereco endereco) {
        logger.info("UsuarioService -> insereCliente");
        var cliente = new Cliente(id, nome, endereco);
        this.clienteRepository.save(cliente);
    }
}

