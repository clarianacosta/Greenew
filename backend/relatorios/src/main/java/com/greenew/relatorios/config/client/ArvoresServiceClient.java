package com.greenew.relatorios.config.client;

import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class ArvoresServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${service.arvores.name:ARVORES-SERVICE}")
    private String arvoresServiceName;

    public ArvoresServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Busca a lista completa de árvores disponíveis no serviço de árvores.
     *
     * @return Uma lista de ArvoreResponseDTO.
     */
    public List<ArvoreResponseDTO> buscarTodasArvores() {
        String url = String.format("lb://%s/api/arvores", arvoresServiceName);

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToFlux(ArvoreResponseDTO.class) // Converte a resposta JSON em um fluxo de objetos
                .collectList() // Agrupa o fluxo em uma lista
                .block(); // Espera a lista completa ser recebida
    }

    /**
     * Busca uma árvore específica pelo seu ID.
     *
     * @param arvoreId O ID da árvore.
     * @return Um ArvoreResponseDTO com os detalhes da árvore.
     * @throws RecursoNaoEncontradoException se o serviço de árvores retornar 404.
     */
    public ArvoreResponseDTO buscarArvorePorId(UUID arvoreId) {
        String url = String.format("lb://%s/api/arvores/%s", arvoresServiceName, arvoreId);

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                // Mapeia o status 404 para uma exceção específica
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(new RecursoNaoEncontradoException("Árvore não encontrada no serviço de árvores com ID: " + arvoreId)))
                .bodyToMono(ArvoreResponseDTO.class) // Converte para um único objeto
                .block(); // Espera o objeto ser recebido
    }
}