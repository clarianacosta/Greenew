import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TerrenoRequest, TerrenoResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class TerrenoService {

  // URL Base: http://localhost:8762/api/terrenos
  private readonly API_URL = `${environment.apiUrl}/terrenos`;

  constructor(private http: HttpClient) {}

  /**
   * Cadastra um novo terreno e o vincula a um produtor existente.
   * Endpoint: POST /api/terrenos
   */
  criar(terreno: TerrenoRequest): Observable<TerrenoResponse> {
    return this.http.post<TerrenoResponse>(this.API_URL, terreno);
  }

  /**
   * Lista todos os terrenos disponíveis na plataforma.
   * Endpoint: GET /api/terrenos
   */
  listarTodos(): Observable<TerrenoResponse[]> {
    return this.http.get<TerrenoResponse[]>(this.API_URL);
  }

  /**
   * Busca detalhes de um terreno específico (inclui dados resumidos do produtor e clima/bioma).
   * Endpoint: GET /api/terrenos/{id}
   */
  buscarPorId(id: string): Observable<TerrenoResponse> {
    return this.http.get<TerrenoResponse>(`${this.API_URL}/${id}`);
  }

  /**
   * Atualiza dados do terreno (área, localização).
   * Endpoint: PUT /api/terrenos/{id}
   */
  atualizar(id: string, terreno: TerrenoRequest): Observable<TerrenoResponse> {
    return this.http.put<TerrenoResponse>(`${this.API_URL}/${id}`, terreno);
  }

  /**
   * Exclui um terreno.
   * Endpoint: DELETE /api/terrenos/{id}
   */
  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
