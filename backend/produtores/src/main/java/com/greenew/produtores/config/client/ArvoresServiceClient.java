package com.greenew.produtores.config.client;

import java.util.UUID;

import com.greenew.produtores.config.dtos.BiomaResponseDTO;
import com.greenew.produtores.config.dtos.ClimaResponseDTO;
import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ArvoresServiceClient {

    // Nome do serviço registrado no Eureka
    @Value("${spring.cloud.arvores-service.name:ARVORES-SERVICE}")
    private String arvoresServiceName;

    private final WebClient.Builder webClientBuilder;

    public ArvoresServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Verifica a existência de um Bioma pelo ID no Arvores-Service.
     */
    public boolean biomaExiste(UUID biomaId) {
        try {
            // Usa lb://<SERVICE-ID> para que o Eureka resolva o endereço
            webClientBuilder.build().get()
                    .uri("lb://" + arvoresServiceName + "/api/biomas/" + biomaId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                        // Se receber 404, o Bioma não existe
                        throw new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null);
                    })
                    .bodyToMono(Void.class) // Não precisamos do corpo, apenas do status
                    .block(); // Chama de forma bloqueante (síncrona)

            return true; // Se não lançar exceção, o status foi 200 OK
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw new RuntimeException("Erro ao consultar serviço de Bioma", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro de comunicação com o Arvores-Service", e);
        }
    }

    /**
     * Verifica a existência de um Clima pelo ID no Arvores-Service.
     */
    public boolean climaExiste(UUID climaId) {
        try {
            // Repete a lógica para o endpoint de Climas
            webClientBuilder.build().get()
                    .uri("lb://" + arvoresServiceName + "/api/climas/" + climaId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                        throw new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null);
                    })
                    .bodyToMono(Void.class)
                    .block();

            return true;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw new RuntimeException("Erro ao consultar serviço de Clima", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro de comunicação com o Arvores-Service", e);
        }
    }

    /**
     * Busca o Bioma completo por ID.
     * @return BiomaResponseDTO se encontrado.
     * @throws RecursoNaoEncontradoException se o bioma não existir.
     */
    public BiomaResponseDTO buscarBiomaPorId(UUID biomaId) {
        try {
            return webClientBuilder.build().get()
                    .uri("lb://" + arvoresServiceName + "/api/biomas/" + biomaId)
                    .retrieve()
                    // Se receber 4xx (como 404), lança uma exceção
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                        return Mono.error(new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + biomaId));
                    })
                    .bodyToMono(BiomaResponseDTO.class) // Espera o objeto BiomaResponseDTO
                    .block();
        } catch (RuntimeException e) {
            // Re-lança a exceção se for RecursoNaoEncontradoException
            if (e.getCause() instanceof RecursoNaoEncontradoException) {
                throw (RecursoNaoEncontradoException) e.getCause();
            }
            throw e;
        }
    }

    /**
     * Busca o Clima completo por ID.
     * @return ClimaResponseDTO se encontrado.
     * @throws RecursoNaoEncontradoException se o clima não existir.
     */
    public ClimaResponseDTO buscarClimaPorId(UUID climaId) {
        try {
            return webClientBuilder.build().get()
                    .uri("lb://" + arvoresServiceName + "/api/climas/" + climaId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                        return Mono.error(new RecursoNaoEncontradoException("Clima não encontrado com ID: " + climaId));
                    })
                    .bodyToMono(ClimaResponseDTO.class) // Espera o objeto ClimaResponseDTO
                    .block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof RecursoNaoEncontradoException) {
                throw (RecursoNaoEncontradoException) e.getCause();
            }
            throw e;
        }
    }
}