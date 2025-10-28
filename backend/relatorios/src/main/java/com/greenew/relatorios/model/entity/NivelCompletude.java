package com.greenew.relatorios.model.entity;

/**
 * Enum que define os níveis de completude de um inventário de GEE,
 * indicando quais escopos foram considerados no cálculo.
 */
public enum NivelCompletude {
    /**
     * O relatório considera apenas as emissões de Escopo 1 (diretas) e
     * Escopo 2 (indiretas por energia). Representa a pegada de carbono operacional.
     */
    OPERACIONAL,

    /**
     * O relatório considera os Escopos 1, 2 e as categorias relevantes do Escopo 3,
     * representando a pegada de carbono completa da cadeia de valor.
     */
    COMPLETO
}