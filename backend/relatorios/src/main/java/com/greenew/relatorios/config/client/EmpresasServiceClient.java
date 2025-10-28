package com.greenew.relatorios.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Service
public class EmpresasServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${service.empresas.name:EMPRESAS-SERVICE}")
    private String empresasServiceName;

    public EmpresasServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Verifica se uma empresa existe fazendo uma chamada HEAD para o endpoint.
     * Esta é a forma mais eficiente, pois não trafega o corpo da resposta.
     *
     * @param empresaId O ID da empresa a ser verificado.
     * @return true se a empresa existe (recebe status 200 OK), false se não existe (404 Not Found).
     */
    public boolean verificarSeEmpresaExiste(UUID empresaId) {
        String url = String.format("lb://%s/api/empresas/%s", empresasServiceName, empresaId);

        try {
            webClientBuilder.build()
                    .head() // Usamos o método HEAD para máxima eficiência
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity() // Não precisamos do corpo, apenas do status
                    .block(); // Bloqueia até a resposta chegar
            return true; // Se não lançou exceção, a empresa existe
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false; // É um 404, então a empresa não existe
            }
            // Para outros erros (500, etc.), lançamos a exceção para sinalizar um problema
            throw new RuntimeException("Erro ao consultar o serviço de empresas.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro de comunicação com o Empresas-Service.", e);
        }
    }
}