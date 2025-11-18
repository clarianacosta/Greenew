import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  RelatorioRequest,
  RelatorioResponse,
  RelatorioCalculado,
  RecomendacaoRanqueada
} from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {

  // URL Base: http://localhost:8762/api/relatorios
  private readonly API_URL = `${environment.apiUrl}/relatorios`;

  constructor(private http: HttpClient) {}

  // ==========================================
  // 1. GESTÃO BÁSICA DO RELATÓRIO
  // ==========================================

  /**
   * Passo 1: Cria o "esqueleto" do relatório vinculado a uma empresa e ano.
   * Ainda sem cálculos de emissão.
   * Endpoint: POST /api/relatorios
   */
  criar(dadosIniciais: RelatorioRequest): Observable<RelatorioResponse> {
    return this.http.post<RelatorioResponse>(this.API_URL, dadosIniciais);
  }

  /**
   * Busca um relatório pelo ID. Pode trazer status 'OPERACIONAL' ou 'COMPLETO'.
   * Endpoint: GET /api/relatorios/{id}
   */
  buscarPorId(id: string): Observable<RelatorioResponse> {
    return this.http.get<RelatorioResponse>(`${this.API_URL}/${id}`);
  }

  /**
   * Lista todos os relatórios de uma empresa específica.
   * Endpoint: GET /api/relatorios?empresaId={uuid}
   */
  listarPorEmpresa(empresaId: string): Observable<RelatorioResponse[]> {
    // Monta a Query String de forma segura
    const params = new HttpParams().set('empresaId', empresaId);
    return this.http.get<RelatorioResponse[]>(this.API_URL, { params });
  }

  // ==========================================
  // 2. LÓGICA DE CÁLCULO E RECOMENDAÇÃO
  // ==========================================

  /**
   * Passo 2: Realiza o cálculo das emissões de CO2e.
   * O usuário seleciona os Escopos (1, 2 e/ou 3) que deseja calcular.
   * * Endpoint: POST /api/relatorios/{id}/calcular
   * Body: Array de números (ex: [1, 2]) representando os escopos.
   */
  calcularEmissoes(relatorioId: string, escopos: number[]): Observable<RelatorioCalculado> {
    return this.http.post<RelatorioCalculado>(
      `${this.API_URL}/${relatorioId}/calcular`,
      escopos
    );
  }

  /**
   * Passo 3: Busca sugestões de compensação baseadas no cálculo realizado.
   * O backend retorna um ranking das melhores combinações (Árvore X Custo X Área).
   * * Endpoint: GET /api/relatorios/{id}/recomendacoes
   */
  buscarRecomendacoes(relatorioId: string): Observable<RecomendacaoRanqueada[]> {
    return this.http.get<RecomendacaoRanqueada[]>(
      `${this.API_URL}/${relatorioId}/recomendacoes`
    );
  }

  /**
   * Passo 4 (Final): O usuário escolhe uma das recomendações (selecionando a árvore).
   * Isso finaliza o relatório e salva a escolha no banco.
   * * Endpoint: POST /api/relatorios/{id}/atribuir-recomendacao?arvoreId={uuid}
   */
  atribuirRecomendacao(relatorioId: string, arvoreId: string): Observable<RelatorioResponse> {
    const params = new HttpParams().set('arvoreId', arvoreId);

    // O corpo é null pois o dado importante vai na URL (Query Param)
    return this.http.post<RelatorioResponse>(
      `${this.API_URL}/${relatorioId}/atribuir-recomendacao`,
      null,
      { params }
    );
  }
}
