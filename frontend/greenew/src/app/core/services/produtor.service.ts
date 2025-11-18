import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProdutorRequest, ProdutorResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ProdutorService {

  // URL Base: http://localhost:8762/api/produtores
  private readonly API_URL = `${environment.apiUrl}/produtores`;

  constructor(private http: HttpClient) {}

  /**
   * Cadastra um novo produtor parceiro.
   * Endpoint: POST /api/produtores
   */
  criar(produtor: ProdutorRequest): Observable<ProdutorResponse> {
    return this.http.post<ProdutorResponse>(this.API_URL, produtor);
  }

  /**
   * Lista todos os produtores cadastrados.
   * Endpoint: GET /api/produtores
   */
  listarTodos(): Observable<ProdutorResponse[]> {
    return this.http.get<ProdutorResponse[]>(this.API_URL);
  }

  /**
   * Busca um produtor pelo ID (inclui a lista de terrenos dele no retorno).
   * Endpoint: GET /api/produtores/{id}
   */
  buscarPorId(id: string): Observable<ProdutorResponse> {
    return this.http.get<ProdutorResponse>(`${this.API_URL}/${id}`);
  }

  /**
   * Atualiza dados cadastrais do produtor (nome, email, celular).
   * Endpoint: PUT /api/produtores/{id}
   */
  atualizar(id: string, produtor: ProdutorRequest): Observable<ProdutorResponse> {
    return this.http.put<ProdutorResponse>(`${this.API_URL}/${id}`, produtor);
  }

  /**
   * Remove um produtor.
   * Nota: O backend pode impedir se houver terrenos vinculados.
   * Endpoint: DELETE /api/produtores/{id}
   */
  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
