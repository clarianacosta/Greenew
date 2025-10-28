package com.greenew.relatorios.config.client;

import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

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
}