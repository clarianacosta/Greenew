package com.greenew.empresas.repository;

import com.greenew.empresas.model.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório para a entidade EmpresaEntity.
 * Estende JpaRepository para herdar operações CRUD padrão.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, UUID> {

    /**
     * Verifica de forma otimizada se já existe uma empresa com o CNPJ fornecido.
     * O Spring Data JPA cria a query automaticamente a partir do nome do método.
     *
     * @param cnpj O CNPJ a ser verificado.
     * @return true se o CNPJ já existe, false caso contrário.
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Busca uma empresa pelo seu CNPJ.
     * O Spring Data JPA cria a query automaticamente a partir do nome do método.
     *
     * @param cnpj O CNPJ da empresa a ser buscada.
     * @return um Optional contendo a EmpresaEntity se encontrada, ou um Optional vazio.
     */
    Optional<EmpresaEntity> findByCnpj(String cnpj);
}