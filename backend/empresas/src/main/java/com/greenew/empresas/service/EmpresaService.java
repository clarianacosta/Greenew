package com.greenew.empresas.service;

import com.greenew.empresas.model.dto.EmpresaRequestDTO;
import com.greenew.empresas.model.dto.EmpresaResponseDTO;
import java.util.List;
import java.util.UUID;

/**
 * Interface que define o contrato para a lógica de negócio do serviço de Empresas.
 * Versão simplificada focada exclusivamente no CRUD de empresas.
 */
public interface EmpresaService {

    /**
     * Cria uma nova empresa no sistema.
     * Valida se o CNPJ já está em uso.
     *
     * @param requestDTO DTO com os dados da empresa a ser criada.
     * @return O DTO da empresa recém-criada.
     */
    EmpresaResponseDTO criarEmpresa(EmpresaRequestDTO requestDTO);

    /**
     * Busca uma empresa pelo seu ID.
     *
     * @param id O UUID da empresa.
     * @return O DTO da empresa encontrada.
     */
    EmpresaResponseDTO buscarPorId(UUID id);

    /**
     * Retorna uma lista de todas as empresas cadastradas.
     *
     * @return Uma lista de DTOs de todas as empresas.
     */
    List<EmpresaResponseDTO> buscarTodas();

    /**
     * Atualiza os dados de uma empresa existente.
     *
     * @param id O UUID da empresa a ser atualizada.
     * @param requestDTO DTO com os novos dados.
     * @return O DTO da empresa com os dados atualizados.
     */
    EmpresaResponseDTO atualizarEmpresa(UUID id, EmpresaRequestDTO requestDTO);

    /**
     * Deleta uma empresa do sistema pelo seu ID.
     *
     * @param id O UUID da empresa a ser deletada.
     */
    void deletarEmpresa(UUID id);
}