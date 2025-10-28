package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.entity.AtividadeEmissoraEntity;
import com.greenew.relatorios.model.entity.FatorEmissaoEntity;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import com.greenew.relatorios.repository.FatorEmissaoRepository;
import com.greenew.relatorios.service.CalculoGHGService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
public class CalculoGHGServiceImpl implements CalculoGHGService {

    private final FatorEmissaoRepository fatorEmissaoRepository;
    private static final BigDecimal GWP_CH4 = new BigDecimal("28");
    private static final BigDecimal GWP_N2O = new BigDecimal("265");

    public CalculoGHGServiceImpl(FatorEmissaoRepository fatorEmissaoRepository) {
        this.fatorEmissaoRepository = fatorEmissaoRepository;
    }

    @Override
    public BigDecimal calcularEmissoes(RelatorioGHGEntity relatorio, Set<Integer> escopos) {
        BigDecimal totalKgCo2e = relatorio.getAtividades().stream()
                .filter(atividade -> escopos.contains(atividade.getEscopo()))
                .map(this::calcularEmissaoIndividual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalKgCo2e.divide(new BigDecimal("1000"), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularEmissaoIndividual(AtividadeEmissoraEntity atividade) {
        FatorEmissaoEntity fator = fatorEmissaoRepository
                .findByNomeAtividadeAndUnidade(atividade.getNome(), atividade.getUnidade())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fator de emissão não encontrado para atividade: '" + atividade.getNome() +
                                "' com unidade: '" + atividade.getUnidade() + "'"));

        BigDecimal quantidade = atividade.getQuantidade();
        BigDecimal emissaoCo2 = quantidade.multiply(fator.getFatorCo2());
        BigDecimal emissaoCh4 = quantidade.multiply(fator.getFatorCh4());
        BigDecimal emissaoN2o = quantidade.multiply(fator.getFatorN2o());
        BigDecimal co2e_Ch4 = emissaoCh4.multiply(GWP_CH4);
        BigDecimal co2e_N2o = emissaoN2o.multiply(GWP_N2O);

        return emissaoCo2.add(co2e_Ch4).add(co2e_N2o);
    }
}