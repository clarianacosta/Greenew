package com.greenew.relatorios.config.client;

import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoresServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${service.produtores.name:PRODUTORES-SERVICE}")
    private String produtoresServiceName;

    public ProdutoresServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Busca os dados de um terreno específico pelo seu ID.
     *
     * @param terrenoId O ID do terreno.
     * @return Um TerrenoResponseDTO com os detalhes do terreno.
     * @throws RecursoNaoEncontradoException se o serviço de produtores retornar 404.
     */
    public TerrenoResponseDTO buscarTerrenoPorId(UUID terrenoId) {
        String url = String.format("lb://%s/api/terrenos/%s", produtoresServiceName, terrenoId);

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                // Mapeia o status 404 para uma exceção específica do nosso domínio
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(new RecursoNaoEncontradoException("Terreno não encontrado no serviço de produtores com ID: " + terrenoId)))
                .bodyToMono(TerrenoResponseDTO.class) // Converte para um único objeto
                .block(); // Espera o objeto ser recebido
    }

    /**
     * Busca a lista completa de terrenos disponíveis.
     */
    public List<TerrenoResponseDTO> buscarTodosTerrenos() {
        String url = String.format("lb://%s/api/terrenos", produtoresServiceName);

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToFlux(TerrenoResponseDTO.class)
                .collectList()
                .block();
    }
}