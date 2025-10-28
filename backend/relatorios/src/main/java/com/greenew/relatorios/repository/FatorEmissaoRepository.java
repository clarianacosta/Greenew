package com.greenew.relatorios.repository;

import com.greenew.relatorios.model.entity.FatorEmissaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório para a entidade FatorEmissaoEntity.
 */
@Repository
public interface FatorEmissaoRepository extends JpaRepository<FatorEmissaoEntity, UUID> {

    /**
     * Busca um fator de emissão pela combinação única de nome da atividade e unidade.
     * Essencial para o serviço de cálculo encontrar o fator correto.
     *
     * @param nome A chave da atividade (ex: "Diesel Rodoviário").
     * @param unidade A unidade da atividade (ex: "Litro").
     * @return um Optional contendo o FatorEmissaoEntity correspondente.
     */
    Optional<FatorEmissaoEntity> findByNomeAtividadeAndUnidade(String nome, String unidade);

    /**
     * Verifica de forma otimizada se um fator já existe com a combinação de nome e unidade.
     * Usado na camada de serviço para evitar a criação de fatores duplicados.
     *
     * @param nome O nome da atividade.
     * @param unidade A unidade da atividade.
     * @return true se a combinação já existir, false caso contrário.
     */
    boolean existsByNomeAtividadeAndUnidade(String nome, String unidade);
}