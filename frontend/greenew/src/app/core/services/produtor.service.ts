import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProdutorRequest, ProdutorResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ProdutorService {
  private readonly API_URL = `${environment.apiUrl}/produtores`;

  constructor(private http: HttpClient) {}

  criar(produtor: ProdutorRequest): Observable<ProdutorResponse> {
    return this.http.post<ProdutorResponse>(this.API_URL, produtor);
  }

  listarTodos(): Observable<ProdutorResponse[]> {
    return this.http.get<ProdutorResponse[]>(this.API_URL);
  }

  buscarPorId(id: string): Observable<ProdutorResponse> {
    return this.http.get<ProdutorResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: string, produtor: ProdutorRequest): Observable<ProdutorResponse> {
    return this.http.put<ProdutorResponse>(`${this.API_URL}/${id}`, produtor);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
