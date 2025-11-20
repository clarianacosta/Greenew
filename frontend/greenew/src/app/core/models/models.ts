// Árvores, Biomas e Climas
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

// Produtores e Terrenos
export interface ProdutorRequest {
  nomeCompleto: string;
  email: string;
  celular?: string;
}

export interface ProdutorResume {
  id: string;
  nomeCompleto: string;
  email?: string;
  celular?: string;
}

export interface ProdutorResponse {
  id: string;
  nomeCompleto: string;
  email: string;
  celular?: string;
  terrenos?: TerrenoResponse[];
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
  produtor: ProdutorResume;
  latitude: number;
  longitude: number;
  areaDisponivelHectares: number;
  biomaLocal: Bioma;
  climaLocal: Clima;
}

// Empresas
export interface EmpresaRequest {
  razaoSocial: string;
  cnpj: string;
}

export interface EmpresaResponse {
  id: string;
  razaoSocial: string;
  cnpj: string;
}

// Relatórios e Cálculos
export interface FatorEmissao {
  id: string;
  nomeAtividade: string;
  unidade: string;
  escopo: number;
  fonte: string;
  fatorCo2: number;
  fatorCh4: number;
  fatorN2o: number;
}

export interface AtividadeRequest {
  fatorEmissaoId: string;
  quantidade: number;
}

export interface AtividadeResponse {
  id: string;
  nome: string;
  escopo: number;
  unidade: string;
  quantidade: number;
}

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
  atividades?: AtividadeResponse[];
  arvoreRecomendada?: string;
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
