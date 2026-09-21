package br.com.fiap.TodosRestaurantes.config;

import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class RequestMappingCustomizado extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        ControleVersionamentoApi apiVersion = AnnotationUtils.findAnnotation(handlerType, ControleVersionamentoApi.class);
        return apiVersion != null ? new VersionamentoApiRequestCondition(apiVersion.value()) : null;
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        ControleVersionamentoApi apiVersion = AnnotationUtils.findAnnotation(method, ControleVersionamentoApi.class);
        return apiVersion != null ? new VersionamentoApiRequestCondition(apiVersion.value()) : null;
    }
}
