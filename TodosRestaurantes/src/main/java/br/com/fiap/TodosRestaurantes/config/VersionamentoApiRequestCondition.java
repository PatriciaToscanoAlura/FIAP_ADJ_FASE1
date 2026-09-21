package br.com.fiap.TodosRestaurantes.config;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

public class VersionamentoApiRequestCondition implements RequestCondition<VersionamentoApiRequestCondition> {
    private final String versao;
    private static final String HEADER_NAME = "x-version";

    public VersionamentoApiRequestCondition(String versao) {
        this.versao = versao;
    }

    @Override
    public VersionamentoApiRequestCondition combine(VersionamentoApiRequestCondition other) {
        return new VersionamentoApiRequestCondition(other.versao);
    }

    @Override
    public @Nullable VersionamentoApiRequestCondition getMatchingCondition(HttpServletRequest request) {
        String headerVersion = request.getHeader(HEADER_NAME);
        if (versao.equals(headerVersion)) {
            return this;
        }
        return null;
    }

    @Override
    public int compareTo(VersionamentoApiRequestCondition other, HttpServletRequest request) {
        return other.versao.compareTo(this.versao);
    }
}