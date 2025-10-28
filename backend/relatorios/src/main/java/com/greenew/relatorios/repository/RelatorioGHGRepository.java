package com.greenew.relatorios.repository;

import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repositório para a entidade RelatorioGHGEntity.
 */
@Repository
public interface RelatorioGHGRepository extends JpaRepository<RelatorioGHGEntity, UUID> {

    /**
     * Busca todos os relatórios associados a um ID de empresa específico.
     * O Spring Data JPA cria a query automaticamente baseando-se no nome do método.
     * Útil para exibir o histórico de relatórios de uma empresa.
     *
     * @param empresaId O ID da empresa.
     * @return Uma lista de RelatorioGHGEntity.
     */
    List<RelatorioGHGEntity> findAllByEmpresaId(UUID empresaId);
}