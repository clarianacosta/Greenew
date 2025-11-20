import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TerrenoRequest, TerrenoResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class TerrenoService {
  private readonly API_URL = `${environment.apiUrl}/terrenos`;

  constructor(private http: HttpClient) {}

  criar(terreno: TerrenoRequest): Observable<TerrenoResponse> {
    return this.http.post<TerrenoResponse>(this.API_URL, terreno);
  }

  listarTodos(): Observable<TerrenoResponse[]> {
    return this.http.get<TerrenoResponse[]>(this.API_URL);
  }

  buscarPorId(id: string): Observable<TerrenoResponse> {
    return this.http.get<TerrenoResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: string, terreno: TerrenoRequest): Observable<TerrenoResponse> {
    return this.http.put<TerrenoResponse>(`${this.API_URL}/${id}`, terreno);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
