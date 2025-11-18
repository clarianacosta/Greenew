import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ArvoreRequest,
  ArvoreResponse,
  Bioma,
  Clima
} from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ArvoreService {

  // URL Base: http://localhost:8762/api/arvores
  private readonly API_URL = `${environment.apiUrl}/arvores`;

  // URLs Auxiliares (Mapeadas no Gateway para o ARVORES-SERVICE)
  private readonly API_BIOMAS = `${environment.apiUrl}/biomas`;
  private readonly API_CLIMAS = `${environment.apiUrl}/climas`;

  constructor(private http: HttpClient) {}

  // ==========================================
  // OPERAÇÕES DE ÁRVORES (CRUD)
  // ==========================================

  /**
   * Cria uma nova espécie de árvore no sistema.
   * Endpoint: POST /api/arvores
   * @param arvore Objeto com os dados da árvore (IDs de biomas/climas inclusos)
   */
  criar(arvore: ArvoreRequest): Observable<ArvoreResponse> {
    return this.http.post<ArvoreResponse>(this.API_URL, arvore);
  }

  /**
   * Retorna a lista completa de árvores cadastradas.
   * Endpoint: GET /api/arvores
   */
  listarTodas(): Observable<ArvoreResponse[]> {
    return this.http.get<ArvoreResponse[]>(this.API_URL);
  }

  /**
   * Busca os detalhes de uma árvore específica pelo ID.
   * Endpoint: GET /api/arvores/{id}
   */
  buscarPorId(id: string): Observable<ArvoreResponse> {
    return this.http.get<ArvoreResponse>(`${this.API_URL}/${id}`);
  }

  /**
   * Atualiza os dados de uma árvore existente.
   * Endpoint: PUT /api/arvores/{id}
   */
  atualizar(id: string, arvore: ArvoreRequest): Observable<ArvoreResponse> {
    return this.http.put<ArvoreResponse>(`${this.API_URL}/${id}`, arvore);
  }

  /**
   * Remove uma árvore do sistema.
   * Endpoint: DELETE /api/arvores/{id}
   */
  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  // ==========================================
  // OPERAÇÕES AUXILIARES (BIOMAS E CLIMAS)
  // Úteis para preencher <select> nos formulários
  // ==========================================

  /**
   * Lista todos os biomas disponíveis.
   * Endpoint: GET /api/biomas
   */
  listarBiomas(): Observable<Bioma[]> {
    return this.http.get<Bioma[]>(this.API_BIOMAS);
  }

  /**
   * Lista todos os climas disponíveis.
   * Endpoint: GET /api/climas
   */
  listarClimas(): Observable<Clima[]> {
    return this.http.get<Clima[]>(this.API_CLIMAS);
  }
}
