// ==========================================
// 1. ÁRVORES, BIOMAS E CLIMAS
// ==========================================

export interface Bioma {
  id: string;
  nome: string;
  descricao?: string;
}

export interface Clima {
  id: string;
  nome: string;
  descricao?: string;
}

export interface ArvoreRequest {
  nomePopular: string;
  nomeCientifico: string;
  taxaAbsorcaoCo2Anual: number;
  custoMedioMuda: number;
  tempoMaturidadeAnos: number;
  alturaMediaM: number;
  diametroCopaMedioM: number;
  biomasIds: string[];
  climasIds: string[];
}

export interface ArvoreResponse {
  id: string;
  nomePopular: string;
  nomeCientifico: string;
  taxaAbsorcaoCo2Anual: number;
  custoMedioMuda: number;
  tempoMaturidadeAnos: number;
  alturaMediaM: number;
  diametroCopaMedioM: number;
  biomas: Bioma[];
  climas: Clima[];
}

// ==========================================
// 2. PRODUTORES E TERRENOS
// ==========================================

export interface ProdutorRequest {
  nomeCompleto: string;
  email: string;
  celular?: string;
}

export interface ProdutorResume {
  id: string;
  nomeCompleto: string;
}

export interface ProdutorResponse {
  id: string;
  nomeCompleto: string;
  email: string;
  celular?: string;
  terrenos?: TerrenoResponse[]; // Lista de terrenos do produtor
}

export interface TerrenoRequest {
  latitude: number;
  longitude: number;
  biomaIdLocal: string;
  climaIdLocal: string;
  areaDisponivelHectares: number;
  produtorId: string;
}

export interface TerrenoResponse {
  id: string;
  produtor: ProdutorResume; // Objeto aninhado
  latitude: number;
  longitude: number;
  areaDisponivelHectares: number;
  biomaLocal: Bioma; // Objeto completo retornado pelo backend
  climaLocal: Clima; // Objeto completo retornado pelo backend
}

// ==========================================
// 3. EMPRESAS
// ==========================================

export interface EmpresaRequest {
  razaoSocial: string;
  cnpj: string;
}

export interface EmpresaResponse {
  id: string;
  razaoSocial: string;
  cnpj: string;
}

// ==========================================
// 4. RELATÓRIOS E CÁLCULOS (GHG)
// ==========================================

export type NivelCompletude = 'COMPLETO' | 'OPERACIONAL';

export interface RelatorioRequest {
  empresaId: string;
  anoReferencia: number;
}

export interface RelatorioCalculado {
  id: string;
  anoReferencia: number;
  emissaoCalculadaCo2e: number;
  nivel: NivelCompletude;
}

export interface RelatorioResponse {
  id: string;
  anoReferencia: number;
  emissaoCalculadaCo2e: number;
  nivel: NivelCompletude;

  // Campos de recomendação que vêm nulos antes de finalizar, mas preenchidos depois
  arvoreRecomendada?: string; // Nome da árvore (String no Java)
  quantidadeNecessaria?: number;
  custoTotalEstimado?: number;
  terrenosCompativeis?: TerrenoResponse[];
}

export interface RecomendacaoRanqueada {
  rank: string;
  arvore: ArvoreResponse;
  quantidadeNecessaria: number;
  custoTotalEstimado: number;
  pontuacao: number;
  areaTotalNecessariaHectares: number;
  terrenosCompativeis: TerrenoResponse[];
}
