import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser'; // <--- Importe Sanitizer
import { Observable, map, of } from 'rxjs';

import { EmpresaNavbar } from '../../../../shared/empresa-navbar/empresa-navbar';
import { RelatorioService } from '../../../../core/services/relatorio.service';
import { RelatorioResponse, TerrenoResponse } from '../../../../core/models/models';

@Component({
  selector: 'app-empresa-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, EmpresaNavbar],
  templateUrl: './empresa-dashboard.html'
})
export class EmpresaDashboard implements OnInit {
  private relatorioService = inject(RelatorioService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private sanitizer = inject(DomSanitizer); // <--- Injete o Sanitizer

  // Dados
  relatorios$: Observable<RelatorioResponse[]> = of([]);
  totalEmitido$: Observable<number> = of(0);
  totalCompensado$: Observable<number> = of(0);

  // Estado dos Modais
  relatorioSelecionado: RelatorioResponse | null = null;
  loadingDetalhes = false;
  urlMapaSegura: SafeResourceUrl | null = null; // <--- Variável do Mapa

  ngOnInit(): void {
    const empresaId = localStorage.getItem('empresa_logada_id');
    if (!empresaId) {
      this.router.navigate(['/empresa/login']);
      return;
    }
    this.carregarDados(empresaId);
  }

  carregarDados(empresaId: string): void {
    this.relatorios$ = this.relatorioService.listarPorEmpresa(empresaId);

    this.totalEmitido$ = this.relatorios$.pipe(
      map(lista => lista.reduce((acc, rel) => acc + (rel.emissaoCalculadaCo2e || 0), 0))
    );

    this.totalCompensado$ = this.relatorios$.pipe(
      map(lista => lista
        .filter(r => !!r.arvoreRecomendada)
        .reduce((acc, rel) => acc + (rel.emissaoCalculadaCo2e || 0), 0)
      )
    );
  }

  // --- HELPERS VISUAIS ---

  getStatusClass(nivel: string, temCompensacao: boolean): string {
    if (temCompensacao) return 'badge-success text-white';
    if (nivel === 'COMPLETO') return 'badge-info text-white';
    return 'badge-ghost';
  }

  getStatusLabel(nivel: string, temCompensacao: boolean): string {
    if (temCompensacao) return 'Finalizado';
    if (nivel === 'COMPLETO') return 'Calculado';
    return 'Rascunho';
  }

  // --- MAPA ---

  abrirMapa(terreno: TerrenoResponse): void {
    if (!terreno.latitude || !terreno.longitude) return;
    const url = `https://maps.google.com/maps?q=${terreno.latitude},${terreno.longitude}&hl=pt&z=15&output=embed`;
    this.urlMapaSegura = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    (document.getElementById('modal_mapa_dashboard') as any).showModal();
  }

  // --- AÇÕES ---

  abrirDetalhes(relatorio: RelatorioResponse): void {
    this.loadingDetalhes = true;

    this.relatorioService.buscarPorId(relatorio.id).subscribe({
      next: (detalhes) => {
        this.relatorioSelecionado = detalhes;
        this.loadingDetalhes = false;
        (document.getElementById('modal_detalhes') as any).showModal();
        this.cdr.detectChanges();
      },
      error: (err) => {
        alert('Erro ao carregar detalhes: ' + err.message);
        this.loadingDetalhes = false;
      }
    });
  }

  continuarRelatorio(id: string): void {
    this.router.navigate(['/empresa/novo-relatorio', id]);
  }

  deletarRelatorio(id: string): void {
    if (confirm('Tem certeza que deseja excluir este relatório?')) {
      this.relatorioService.deletar(id).subscribe({
        next: () => {
          const empresaId = localStorage.getItem('empresa_logada_id');
          if (empresaId) this.carregarDados(empresaId);
          this.cdr.detectChanges();
        },
        error: (err: any) => alert('Erro ao deletar: ' + err.message)
      });
    }
  }
}
