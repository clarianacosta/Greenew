import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmpresaRequest, EmpresaResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  // URL Base: http://localhost:8762/api/empresas
  private readonly API_URL = `${environment.apiUrl}/empresas`;

  constructor(private http: HttpClient) {}

  /**
   * Registra uma nova empresa cliente.
   * Endpoint: POST /api/empresas
   */
  criar(empresa: EmpresaRequest): Observable<EmpresaResponse> {
    return this.http.post<EmpresaResponse>(this.API_URL, empresa);
  }

  /**
   * Retorna todas as empresas cadastradas.
   * Endpoint: GET /api/empresas
   */
  listarTodas(): Observable<EmpresaResponse[]> {
    return this.http.get<EmpresaResponse[]>(this.API_URL);
  }

  /**
   * Busca empresa por ID.
   * Endpoint: GET /api/empresas/{id}
   */
  buscarPorId(id: string): Observable<EmpresaResponse> {
    return this.http.get<EmpresaResponse>(`${this.API_URL}/${id}`);
  }

  /**
   * Atualiza Razão Social ou CNPJ.
   * Endpoint: PUT /api/empresas/{id}
   */
  atualizar(id: string, empresa: EmpresaRequest): Observable<EmpresaResponse> {
    return this.http.put<EmpresaResponse>(`${this.API_URL}/${id}`, empresa);
  }

  /**
   * Remove uma empresa.
   * Endpoint: DELETE /api/empresas/{id}
   */
  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
