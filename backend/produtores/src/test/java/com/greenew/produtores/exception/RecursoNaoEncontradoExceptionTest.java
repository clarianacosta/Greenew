package com.greenew.produtores.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Testes para a classe RecursoNaoEncontradoException")
class RecursoNaoEncontradoExceptionTest {

    @Test
    @DisplayName("A exceção deve ser instanciada com a mensagem correta")
    void deveSerInstanciadoComAMensagemCorreta() {
        // Cenário
        String mensagem = "Recurso não encontrado com o ID 123";

        // Ação: Lança a exceção e a captura para inspeção
        Throwable thrown = catchThrowable(() -> {
            throw new RecursoNaoEncontradoException(mensagem);
        });

        // Verificação
        assertThat(thrown)
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage(mensagem);
    }

    @Test
    @DisplayName("A exceção deve ter a anotação @ResponseStatus com o código NOT_FOUND")
    void deveTerAnotacaoResponseStatusCorreta() {
        // Cenário: A classe da exceção
        Class<RecursoNaoEncontradoException> exceptionClass = RecursoNaoEncontradoException.class;

        // Ação: Recupera a anotação @ResponseStatus da classe
        ResponseStatus annotation = exceptionClass.getAnnotation(ResponseStatus.class);

        // Verificação
        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}