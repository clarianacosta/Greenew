import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { forkJoin, map, catchError, of } from 'rxjs';

import { EmpresaService } from '../../../../core/services/empresa.service';
import { ProdutorService } from '../../../../core/services/produtor.service';
import { TerrenoService } from '../../../../core/services/terreno.service';
import { ArvoreService } from '../../../../core/services/arvore.service';

interface ItemRecente {
  tipo: 'Empresa' | 'Produtor' | 'Terreno' | 'Árvore';
  nome: string;
  detalhe: string;
  icone: string;
  classeIcone: string;
  link: string;
}

/**
 * Dashboard administrativo do sistema Greenew
 * Exibe resumo das atividades recentes e estatísticas principais
 */
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, AdminNavbar],
  templateUrl: './admin-dashboard.html',
})
export class AdminDashboard implements OnInit {
  /** Serviços injetados */
  private empresaService = inject(EmpresaService);
  private produtorService = inject(ProdutorService);
  private terrenoService = inject(TerrenoService);
  private arvoreService = inject(ArvoreService);
  private cdr = inject(ChangeDetectorRef);

  /** Estado da interface */
  atividadesRecentes: ItemRecente[] = [];
  loading = true;

  ngOnInit(): void {
    this.carregarDadosDashboard();
  }

  /**
   * Carrega dados de todos os serviços e monta lista de atividades recentes
   */
  private carregarDadosDashboard(): void {
    this.loading = true;

    forkJoin({
      terrenos: this.terrenoService.listarTodos().pipe(catchError(() => of([]))),
      arvores: this.arvoreService.listarTodas().pipe(catchError(() => of([]))),
      produtores: this.produtorService.listarTodos().pipe(catchError(() => of([]))),
      empresas: this.empresaService.listarTodas().pipe(catchError(() => of([]))),
    })
      .pipe(
        map((dados) => {
          const lista: ItemRecente[] = [];

          // Adiciona terrenos recentes
          if (dados.terrenos) {
            dados.terrenos.slice(-2).forEach((t) => {
              lista.push({
                tipo: 'Terreno',
                nome: `Proprietário: ${t.produtor?.nomeCompleto}`,
                detalhe: `${t.biomaLocal.nome} | ${t.climaLocal.nome}`,
                icone: 'location_on',
                classeIcone: 'text-pink-500 bg-pink-100',
                link: '/admin/terrenos',
              });
            });
          }

          // Adiciona produtores recentes
          if (dados.produtores) {
            dados.produtores.slice(-2).forEach((p) => {
              lista.push({
                tipo: 'Produtor',
                nome: p.nomeCompleto,
                detalhe: p.email,
                icone: 'agriculture',
                classeIcone: 'text-blue-600 bg-blue-100',
                link: '/admin/produtores',
              });
            });
          }

          // Adiciona árvores recentes
          if (dados.arvores) {
            dados.arvores.slice(-2).forEach((a) => {
              lista.push({
                tipo: 'Árvore',
                nome: a.nomePopular,
                detalhe: a.nomeCientifico,
                icone: 'forest',
                classeIcone: 'text-emerald-500 bg-emerald-100',
                link: '/admin/arvores',
              });
            });
          }

          // Adiciona empresas recentes
          if (dados.empresas) {
            dados.empresas.slice(-2).forEach((e) => {
              lista.push({
                tipo: 'Empresa',
                nome: e.razaoSocial,
                detalhe: `CNPJ: ${e.cnpj}`,
                icone: 'corporate_fare',
                classeIcone: 'text-yellow-500 bg-yellow-100',
                link: '/admin/empresas',
              });
            });
          }

          return lista.reverse().slice(0, 8);
        })
      )
      .subscribe({
        next: (resultado) => {
          this.atividadesRecentes = resultado;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Erro no dashboard:', err);
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }
}
