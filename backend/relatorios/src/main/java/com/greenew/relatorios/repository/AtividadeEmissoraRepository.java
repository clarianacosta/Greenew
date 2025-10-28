package com.greenew.relatorios.repository;

import com.greenew.relatorios.model.entity.AtividadeEmissoraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repositório para a entidade AtividadeEmissoraEntity.
 * Herda todas as operações CRUD necessárias do JpaRepository.
 */
@Repository
public interface AtividadeEmissoraRepository extends JpaRepository<AtividadeEmissoraEntity, UUID> {
    /**
     * Busca todas as atividades de emissão que pertencem a um relatório específico.
     * O Spring Data JPA cria a query automaticamente pelo nome do método, buscando
     * pelo campo 'relatorio' e, dentro dele, pelo campo 'id'.
     *
     * @param relatorioId O ID do relatório pai.
     * @return Uma lista de AtividadeEmissoraEntity.
     */
    List<AtividadeEmissoraEntity> findAllByRelatorioId(UUID relatorioId);
}