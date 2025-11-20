import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  RelatorioRequest,
  RelatorioResponse,
  RelatorioCalculado,
  RecomendacaoRanqueada,
  FatorEmissao,
  AtividadeRequest,
  AtividadeResponse
} from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {
  private readonly API_URL = `${environment.apiUrl}/relatorios`;
  private readonly API_FATORES = `${environment.apiUrl}/fatores-emissao`;

  constructor(private http: HttpClient) {}

  criar(dadosIniciais: RelatorioRequest): Observable<RelatorioResponse> {
    return this.http.post<RelatorioResponse>(this.API_URL, dadosIniciais);
  }

  buscarPorId(id: string): Observable<RelatorioResponse> {
    return this.http.get<RelatorioResponse>(`${this.API_URL}/${id}`);
  }

  listarPorEmpresa(empresaId: string): Observable<RelatorioResponse[]> {
    const params = new HttpParams().set('empresaId', empresaId);
    return this.http.get<RelatorioResponse[]>(this.API_URL, { params });
  }

  listarFatoresEmissao(): Observable<FatorEmissao[]> {
    return this.http.get<FatorEmissao[]>(this.API_FATORES);
  }

  adicionarAtividade(relatorioId: string, dados: AtividadeRequest): Observable<AtividadeResponse> {
    return this.http.post<AtividadeResponse>(`${this.API_URL}/${relatorioId}/atividades`, dados);
  }

  calcularEmissoes(relatorioId: string, escopos: number[]): Observable<RelatorioCalculado> {
    return this.http.post<RelatorioCalculado>(`${this.API_URL}/${relatorioId}/calcular`, escopos);
  }

  buscarRecomendacoes(relatorioId: string): Observable<RecomendacaoRanqueada[]> {
    return this.http.get<RecomendacaoRanqueada[]>(`${this.API_URL}/${relatorioId}/recomendacoes`);
  }

  atribuirRecomendacao(relatorioId: string, arvoreId: string): Observable<RelatorioResponse> {
    const params = new HttpParams().set('arvoreId', arvoreId);
    return this.http.post<RelatorioResponse>(`${this.API_URL}/${relatorioId}/atribuir-recomendacao`, null, { params });
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  criarFator(fator: Omit<FatorEmissao, 'id'>): Observable<FatorEmissao> {
    return this.http.post<FatorEmissao>(this.API_FATORES, fator);
  }

  deletarFator(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_FATORES}/${id}`);
  }
}
