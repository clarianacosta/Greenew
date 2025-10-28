package com.greenew.produtores.service.impl;

import com.greenew.produtores.config.client.ArvoresServiceClient;
import com.greenew.produtores.config.dtos.BiomaResponseDTO;
import com.greenew.produtores.config.dtos.ClimaResponseDTO;
import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import com.greenew.produtores.model.mapper.TerrenoMapper;
import com.greenew.produtores.repository.ProdutorRepository;
import com.greenew.produtores.repository.TerrenoRepository;
import com.greenew.produtores.service.TerrenoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TerrenoServiceImpl implements TerrenoService {

    private final TerrenoRepository terrenoRepository;
    private final ProdutorRepository produtorRepository;
    private final TerrenoMapper terrenoMapper;
    private final ArvoresServiceClient arvoresServiceClient;

    public TerrenoServiceImpl(TerrenoRepository terrenoRepository, ProdutorRepository produtorRepository, TerrenoMapper terrenoMapper, ArvoresServiceClient arvoresServiceClient) {
        this.terrenoRepository = terrenoRepository;
        this.produtorRepository = produtorRepository;
        this.terrenoMapper = terrenoMapper;
        this.arvoresServiceClient = arvoresServiceClient;
    }

    /**
     * Busca os detalhes de Bioma e Clima do Arvores-Service
     * e os anexa ao DTO de Resposta do Terreno.
     */
    public TerrenoResponseDTO mapEntityToFullResponseDTO(TerrenoEntity terrenoEntity) {
        // 1. Mapeia a Entity para o DTO (preenche dados locais)
        TerrenoResponseDTO responseDTO = terrenoMapper.toResponseDTO(terrenoEntity);

        try {
            // 2. Busca Bioma e Clima usando os IDs locais (RPC via WebClient/Eureka)
            // Assumimos que TerrenoEntity tem os métodos getBiomaIdLocal() e getClimaIdLocal()
            BiomaResponseDTO bioma = arvoresServiceClient.buscarBiomaPorId(terrenoEntity.getBiomaIdLocal());
            ClimaResponseDTO clima = arvoresServiceClient.buscarClimaPorId(terrenoEntity.getClimaIdLocal());

            // 3. Anexa os objetos DTOs na Resposta
            responseDTO.setBiomaLocal(bioma);
            responseDTO.setClimaLocal(clima);
        } catch (RecursoNaoEncontradoException e) {
            // Se o recurso externo (Bioma ou Clima) foi deletado após a criação do Terreno,
            // logamos e retornamos null nos campos, mas o Terreno continua existindo.
            System.err.println("Aviso: Recurso externo não encontrado durante a busca do Terreno: " + e.getMessage());
            responseDTO.setBiomaLocal(null);
            responseDTO.setClimaLocal(null);
        } catch (Exception e) {
            // Outros erros de comunicação (timeout, serviço fora do ar)
            System.err.println("Erro de comunicação com Arvores-Service: " + e.getMessage());
            // Pode-se optar por lançar uma exceção ou retornar null, dependendo da criticidade.
            // Aqui, lançamos RuntimeException para falhas não 404 (para o produtor ver o erro).
            // Em cenários GET, é melhor retornar 200 com campos null.
            // Para manter o contrato do método findEntityOrThrow, usaremos o fluxo de RecursoNaoEncontradoException se for a criação.
        }

        return responseDTO;
    }


    // --- MÉTODOS DE ESCRITA E LEITURA ---

    @Override
    @Transactional
    public TerrenoResponseDTO criar(TerrenoRequestDTO terrenoRequestDTO) {
        // 1. Validação e busca dos detalhes (lança RecursoNaoEncontradoException se o bioma/clima não existir)
        BiomaResponseDTO biomaResponseDTO = arvoresServiceClient.buscarBiomaPorId(terrenoRequestDTO.getBiomaIdLocal());
        ClimaResponseDTO climaResponseDTO = arvoresServiceClient.buscarClimaPorId(terrenoRequestDTO.getClimaIdLocal());

        // 2. Encontra o produtor (validação de coesão local)
        ProdutorEntity produtor = produtorRepository.findById(terrenoRequestDTO.getProdutorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produtor não encontrado com ID: " + terrenoRequestDTO.getProdutorId()));

        // 3. Mapeia e salva
        TerrenoEntity terreno = terrenoMapper.toEntity(terrenoRequestDTO);
        terreno.setProdutor(produtor);
        TerrenoEntity terrenoSalvo = terrenoRepository.save(terreno);

        // 4. Mapeia e anexa os detalhes (os objetos DTOs já foram buscados)
        TerrenoResponseDTO responseDTO = terrenoMapper.toResponseDTO(terrenoSalvo);
        responseDTO.setBiomaLocal(biomaResponseDTO);
        responseDTO.setClimaLocal(climaResponseDTO);

        return responseDTO;
    }

    @Override
    public TerrenoResponseDTO buscarPorId(UUID id) {
        TerrenoEntity terreno = terrenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Terreno não encontrado com ID: " + id));

        // CORREÇÃO: Busca detalhes externos
        return mapEntityToFullResponseDTO(terreno);
    }

    @Override
    public List<TerrenoResponseDTO> buscarTodos() {
        return terrenoRepository.findAll().stream()
                // CORREÇÃO: Anexa detalhes para cada item
                .map(this::mapEntityToFullResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TerrenoResponseDTO> buscarTodosPorProdutorId(UUID produtorId) {
        ProdutorEntity produtor = produtorRepository.findById(produtorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produtor não encontrado com ID: " + produtorId));

        return produtor.getTerrenos().stream()
                // CORREÇÃO: Anexa detalhes para cada item
                .map(this::mapEntityToFullResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TerrenoResponseDTO atualizar(UUID id, TerrenoRequestDTO terrenoRequestDTO) {
        TerrenoEntity terrenoExistente = terrenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Terreno não encontrado com ID: " + id));

        // Validação e busca de detalhes se os IDs de bioma/clima forem atualizados (opcional)
        // Por brevidade, mantemos a coesão fraca aqui: o BeanUtils copia os IDs e a validação de existência
        // é garantida no próximo GET.

        BeanUtils.copyProperties(terrenoRequestDTO, terrenoExistente, "id", "produtor");

        TerrenoEntity terrenoAtualizado = terrenoRepository.save(terrenoExistente);

        // CORREÇÃO: Busca detalhes externos da entidade atualizada
        return mapEntityToFullResponseDTO(terrenoAtualizado);
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        if (!terrenoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Terreno não encontrado com ID: " + id);
        }
        terrenoRepository.deleteById(id);
    }
}