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
  private readonly API_URL = `${environment.apiUrl}/arvores`;
  private readonly API_BIOMAS = `${environment.apiUrl}/biomas`;
  private readonly API_CLIMAS = `${environment.apiUrl}/climas`;

  constructor(private http: HttpClient) {}

  criar(arvore: ArvoreRequest): Observable<ArvoreResponse> {
    return this.http.post<ArvoreResponse>(this.API_URL, arvore);
  }

  listarTodas(): Observable<ArvoreResponse[]> {
    return this.http.get<ArvoreResponse[]>(this.API_URL);
  }

  buscarPorId(id: string): Observable<ArvoreResponse> {
    return this.http.get<ArvoreResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: string, arvore: ArvoreRequest): Observable<ArvoreResponse> {
    return this.http.put<ArvoreResponse>(`${this.API_URL}/${id}`, arvore);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  listarBiomas(): Observable<Bioma[]> {
    return this.http.get<Bioma[]>(this.API_BIOMAS);
  }

  listarClimas(): Observable<Clima[]> {
    return this.http.get<Clima[]>(this.API_CLIMAS);
  }
}
