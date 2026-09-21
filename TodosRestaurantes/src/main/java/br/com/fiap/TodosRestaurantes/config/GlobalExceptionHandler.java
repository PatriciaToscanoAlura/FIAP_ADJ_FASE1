package br.com.fiap.TodosRestaurantes.config;

import br.com.fiap.TodosRestaurantes.dtos.ResponseErroDTO;
import br.com.fiap.TodosRestaurantes.services.exceptions.ErroNaAlteracaoEmail;
import br.com.fiap.TodosRestaurantes.services.exceptions.ErroNaAlteracaoSenha;
import br.com.fiap.TodosRestaurantes.services.exceptions.FuncaoNaoPermitida;
import br.com.fiap.TodosRestaurantes.services.exceptions.LoginIncorreto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(Exception ex) {
        logger.info("Erro genérico: " + ex);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(MethodArgumentNotValidException ex) {
        logger.info("Erro na validação da requisição: " + ex);
        FieldError campoErro = ex.getBindingResult().getFieldError();
        String mensagem = campoErro.getDefaultMessage();
        ResponseErroDTO erro = new ResponseErroDTO(
                mensagem,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(LoginIncorreto.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(LoginIncorreto ex) {
        logger.info("Erro no Login: " + ex);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Login incorreto",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(ErroNaAlteracaoSenha.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(ErroNaAlteracaoSenha ex) {
        logger.info("Erro na alteração da senha: " + ex);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Não foi possível alterar a senha. Tente novamente mais tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(ErroNaAlteracaoEmail.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(ErroNaAlteracaoEmail ex) {
        logger.info("Erro na alteração do e-mail: " + ex);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Não é possível alterar o e-mail.",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(FuncaoNaoPermitida.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(FuncaoNaoPermitida ex) {
        logger.info("Erro função nao permitida: " + ex);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Função não permitida para o usuário",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(DataAccessException ex) {
        String causaRaiz = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";
        if (ex instanceof DataIntegrityViolationException) {
            logger.info("Violação de integridade referencial: " + causaRaiz);
            if (causaRaiz.contains("EMAIL")) {
                ResponseErroDTO erro = new ResponseErroDTO(
                        "Não é possível cadastrar o usuário.",
                        HttpStatus.BAD_REQUEST.value()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
            }
        }
        logger.info("Erro no acesso ao banco de dados: " + causaRaiz);
        ResponseErroDTO erro = new ResponseErroDTO(
                "Ocorreu um erro inesperado no sistema. Comunique o administrador",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseErroDTO> handleGenericException(HttpMessageNotReadableException ex) {
        logger.info("Erro na validação de ENUM: " + ex);
        if (ex.getCause() instanceof InvalidFormatException formatoInvalido) {
            if (formatoInvalido.getTargetType() != null && formatoInvalido.getTargetType().isEnum()) {
                String nomeCampo = formatoInvalido.getPath().isEmpty() ? "campo" : formatoInvalido.getPath().get(0).getPropertyName();
                ResponseErroDTO erro = new ResponseErroDTO(
                        "Conteúdo inválido no " + nomeCampo,
                        HttpStatus.BAD_REQUEST.value()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
            }
        }
        logger.info("Erro na validação de campos");
        ResponseErroDTO erro = new ResponseErroDTO(
                "Dados da requisição não estão corretos.",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}


