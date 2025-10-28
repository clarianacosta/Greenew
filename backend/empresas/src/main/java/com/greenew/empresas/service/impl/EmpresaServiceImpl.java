package com.greenew.empresas.service.impl;

import com.greenew.empresas.exception.RecursoNaoEncontradoException;
import com.greenew.empresas.model.dto.EmpresaRequestDTO;
import com.greenew.empresas.model.dto.EmpresaResponseDTO;
import com.greenew.empresas.model.entity.EmpresaEntity;
import com.greenew.empresas.model.mapper.EmpresaMapper;
import com.greenew.empresas.repository.EmpresaRepository;
import com.greenew.empresas.service.EmpresaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }

    @Override
    public EmpresaResponseDTO criarEmpresa(EmpresaRequestDTO requestDTO) {
        if (empresaRepository.existsByCnpj(requestDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        }
        EmpresaEntity entity = empresaMapper.toEntity(requestDTO);
        EmpresaEntity savedEntity = empresaRepository.save(entity);
        return empresaMapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponseDTO buscarPorId(UUID id) {
        return empresaRepository.findById(id)
                .map(empresaMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada com ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponseDTO> buscarTodas() {
        return empresaRepository.findAll().stream()
                .map(empresaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmpresaResponseDTO atualizarEmpresa(UUID id, EmpresaRequestDTO requestDTO) {
        EmpresaEntity entity = empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada com ID: " + id));

        if (!entity.getCnpj().equals(requestDTO.getCnpj()) && empresaRepository.existsByCnpj(requestDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado para outra empresa.");
        }

        empresaMapper.updateEntityFromDTO(requestDTO, entity);
        return empresaMapper.toResponseDTO(empresaRepository.save(entity));
    }

    @Override
    public void deletarEmpresa(UUID id) {
        if (!empresaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Empresa não encontrada com ID: " + id);
        }
        empresaRepository.deleteById(id);
    }
}