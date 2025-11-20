import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EmpresaRequest, EmpresaResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private readonly API_URL = `${environment.apiUrl}/empresas`;

  constructor(private http: HttpClient) {}

  criar(empresa: EmpresaRequest): Observable<EmpresaResponse> {
    return this.http.post<EmpresaResponse>(this.API_URL, empresa);
  }

  listarTodas(): Observable<EmpresaResponse[]> {
    return this.http.get<EmpresaResponse[]>(this.API_URL);
  }

  buscarPorId(id: string): Observable<EmpresaResponse> {
    return this.http.get<EmpresaResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: string, empresa: EmpresaRequest): Observable<EmpresaResponse> {
    return this.http.put<EmpresaResponse>(`${this.API_URL}/${id}`, empresa);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
