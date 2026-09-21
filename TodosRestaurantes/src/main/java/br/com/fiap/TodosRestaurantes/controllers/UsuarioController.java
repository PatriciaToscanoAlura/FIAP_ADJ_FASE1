package br.com.fiap.TodosRestaurantes.controllers;

import br.com.fiap.TodosRestaurantes.config.ControleVersionamentoApi;
import br.com.fiap.TodosRestaurantes.dtos.*;
import br.com.fiap.TodosRestaurantes.entities.Cliente;
import br.com.fiap.TodosRestaurantes.entities.DonoRestaurante;
import br.com.fiap.TodosRestaurantes.entities.Login;
import br.com.fiap.TodosRestaurantes.services.LoginService;
import br.com.fiap.TodosRestaurantes.services.UsuarioService;
import br.com.fiap.TodosRestaurantes.services.exceptions.ErroNaAlteracaoEmail;
import br.com.fiap.TodosRestaurantes.services.exceptions.ErroNaAlteracaoSenha;
import br.com.fiap.TodosRestaurantes.services.exceptions.FuncaoNaoPermitida;
import br.com.fiap.TodosRestaurantes.services.exceptions.LoginIncorreto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@ControleVersionamentoApi("1")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;
    private final LoginService loginService;

    public UsuarioController(UsuarioService usuarioService, LoginService loginService) {
        this.usuarioService = usuarioService;
        this.loginService = loginService;
    }

   @PostMapping
    public ResponseEntity<Void> saveUsuario(
            @Valid @RequestBody IncluiUsuarioRequestDTO usuario
    ) {
        logger.info("POST -> /usuario - versão 1");
        var loginIncluido = this.loginService.saveLogin(usuario);
        this.usuarioService.saveUsuario(usuario, loginIncluido.getIdUsuario());
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<ResponsePesquisaClienteDTO> findByNome(
            @AuthenticationPrincipal Login usuarioLogado,
            @RequestParam("nome") String nome
    ) {
        logger.info("GET -> /usuario - versão 1");
        var usuario = usuarioService.findUsuarioById(usuarioLogado.getIdUsuario());
        if (usuario instanceof Cliente) throw new FuncaoNaoPermitida("Usuário logado é cliente");
        var clientes = this.usuarioService.findClienteByNome(nome);
        List<ClienteDTO> clienteDTO = new ArrayList<>();
        for (Cliente cliente: clientes) {
            var email = this.loginService.emailById(cliente.getIdUsuario());
            var endereco = this.usuarioService.enderecoById(cliente.getEndereco().getIdEndereco());
            var enderecoDTO = new ResponseEnderecoDTO(endereco.getTipoLogradouro(), endereco.getNomeLogradouro(), endereco.getNumero(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(), endereco.getUf());
            var clienteDaLista = new ClienteDTO(cliente.getNome(), enderecoDTO, cliente.getUltimaAlteracao(), email);
            clienteDTO.add(clienteDaLista);
        }
        var listaCliente = new ResponsePesquisaClienteDTO(clienteDTO);
        return ResponseEntity.ok(listaCliente);
    }

    @GetMapping("/me")
    public ResponseEntity<ClienteDTO> findById(
            @AuthenticationPrincipal Login usuarioLogado
    ) {
        logger.info("GET -> /usuario/me - versão 1");
        var usuario = this.usuarioService.findUsuarioById(usuarioLogado.getIdUsuario());
        if (usuario instanceof Cliente cliente) {
            var endereco = cliente.getEndereco();
            var enderecoDTO = new ResponseEnderecoDTO(endereco.getTipoLogradouro(), endereco.getNomeLogradouro(), endereco.getNumero(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(), endereco.getUf());
            var clienteDTO = new ClienteDTO(cliente.getNome(), enderecoDTO, cliente.getUltimaAlteracao(), usuarioLogado.getEmail());
            return ResponseEntity.ok(clienteDTO);
        }
        if (usuario instanceof DonoRestaurante donoRestaurante) {
            var endereco = donoRestaurante.getEndereco();
            var enderecoDTO = new ResponseEnderecoDTO(endereco.getTipoLogradouro(), endereco.getNomeLogradouro(), endereco.getNumero(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(), endereco.getUf());
            var clienteDTO = new ClienteDTO(donoRestaurante.getNome(), enderecoDTO, donoRestaurante.getUltimaAlteracao(), usuarioLogado.getEmail());
            return ResponseEntity.ok(clienteDTO);
        }
        return ResponseEntity.status(400).build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletaUsuario(
            @AuthenticationPrincipal Login usuarioLogado
    ) {
        logger.info("DELETE -> /usuario/me - versão 1");
        if (usuarioService.deletaUsuarioById(usuarioLogado.getIdUsuario()).equals(1)) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(400).build();
    }

    @PutMapping("/me")
    public ResponseEntity<Void> alteraUsuarioLogado(
            @Valid @RequestBody AlteraUsuarioDTO alteraUsuarioDTO,
            @AuthenticationPrincipal Login usuarioLogado
    ) {
        logger.info("PUT -> /me - versão 1");
        var email = alteraUsuarioDTO.email().trim();
        if (!usuarioLogado.getEmail().equals(email)) {
            logger.info("****Mudou email****");
            var loginEmail = this.loginService.findByEmail(email);
            if (loginEmail.isPresent()) {
                throw new ErroNaAlteracaoEmail("E-mail já está cadastrado para outro usuário");
            }
            this.loginService.alteraEmailLogin(usuarioLogado, email);
        }
        logger.info("Vai alterar dados do usuario");
        var linhas = this.usuarioService.alteraUsuarioById(alteraUsuarioDTO, usuarioLogado.getIdUsuario());
        if (linhas > 0) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(400).build();
    }

    @PutMapping("/me/senha")
    public ResponseEntity<Void> alteraSenha(
            @Valid @RequestBody TrocaSenhaDTO trocaSenhaDTO,
            @AuthenticationPrincipal Login usuarioLogado
    ) {
        logger.info("PUT -> /usuario/me/senha - versão 1");
        if (this.loginService.confirmaLogin(trocaSenhaDTO, usuarioLogado)) {
            String novaSenha = trocaSenhaDTO.novaSenha();
            if (!this.loginService.alteraSenhaLogin(usuarioLogado, novaSenha)) {
                throw new ErroNaAlteracaoSenha("Erro na alteração da senha");
            }
        } else throw new LoginIncorreto("Usuário/Senha atual incorretos");
        return ResponseEntity.status(204).build();
    }
}
