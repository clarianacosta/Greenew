import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { RelatorioService } from '../../../../core/services/relatorio.service';
import { FatorEmissao } from '../../../../core/models/models';

@Component({
  selector: 'app-admin-fatores',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AdminNavbar],
  templateUrl: './admin-fatores.html'
})
export class AdminFatores implements OnInit {
  private service = inject(RelatorioService);

  private refresh$ = new BehaviorSubject<void>(undefined);
  fatores$: Observable<FatorEmissao[]>;

  // Objeto para cadastro
  novoFator: any = {
    nomeAtividade: '',
    unidade: '',
    escopo: 1,
    fonte: '',
    fatorCo2: 0,
    fatorCh4: 0,
    fatorN2o: 0
  };

  constructor() {
    this.fatores$ = this.refresh$.pipe(
      switchMap(() => this.service.listarFatoresEmissao())
    );
  }

  ngOnInit(): void {}

  carregarFatores(): void {
    this.refresh$.next();
  }

  abrirModal(): void {
    // Reseta o formulário
    this.novoFator = { nomeAtividade: '', unidade: '', escopo: 1, fonte: '', fatorCo2: 0, fatorCh4: 0, fatorN2o: 0 };
    (document.getElementById('modal_fator') as any).showModal();
  }

  salvarFator(): void {
    if (!this.novoFator.nomeAtividade || !this.novoFator.unidade) return;

    this.service.criarFator(this.novoFator).subscribe({
      next: () => {
        this.carregarFatores();
        (document.getElementById('modal_fator') as any).close();
      },
      error: (err) => alert('Erro ao salvar: ' + err.message)
    });
  }

  deletarFator(id: string): void {
    if (confirm('Tem certeza que deseja excluir este fator?')) {
      this.service.deletarFator(id).subscribe(() => this.carregarFatores());
    }
  }
}
