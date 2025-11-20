import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, combineLatest, map, switchMap } from 'rxjs';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { ArvoreService } from '../../../../core/services/arvore.service';
import {
  ArvoreResponse,
  ArvoreRequest,
  Bioma,
  Clima
} from '../../../../core/models/models';

/**
 * Componente de administração de árvores
 * Gerencia CRUD de espécies de árvores com busca por nome popular ou científico
 */
@Component({
  selector: 'app-admin-arvores',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AdminNavbar],
  templateUrl: './admin-arvores.html'
})
export class AdminArvores implements OnInit {
  /** Serviços injetados */
  private arvoreService = inject(ArvoreService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  /** Controle de estado */
  private refresh$ = new BehaviorSubject<void>(undefined);
  private termoBusca$ = new BehaviorSubject<string>('');

  /** Interface de busca */
  termoInput: string = '';

  /** Observables de dados */
  arvores$: Observable<ArvoreResponse[]>;
  biomas$: Observable<Bioma[]>;
  climas$: Observable<Clima[]>;

  /** Formulário de árvore */
  novaArvore: ArvoreRequest = this.inicializarFormulario();
  arvoreEmEdicaoId: string | null = null;

  constructor() {
    this.biomas$ = this.arvoreService.listarBiomas();
    this.climas$ = this.arvoreService.listarClimas();

    this.arvores$ = combineLatest([
      this.refresh$.pipe(switchMap(() => this.arvoreService.listarTodas())),
      this.termoBusca$
    ]).pipe(
      map(([lista, termo]) => {
        if (!termo || termo.trim() === '') return lista;

        const termoLower = termo.toLowerCase();
        return lista.filter(a =>
          a.nomePopular.toLowerCase().includes(termoLower) ||
          a.nomeCientifico.toLowerCase().includes(termoLower)
        );
      })
    );
  }

  ngOnInit(): void {
    this.verificarParametrosBusca();
  }

  /**
   * Verifica se há parâmetros de busca na URL
   */
  private verificarParametrosBusca(): void {
    this.route.queryParams.subscribe(params => {
      const busca = params['busca'];
      if (busca) {
        this.termoInput = busca;
        this.filtrar(busca);
      }
    });
  }

  /** Aplica filtro de busca */
  filtrar(termo: string): void {
    this.termoBusca$.next(termo);
  }

  /** Remove filtro de busca */
  limparBusca(): void {
    this.termoInput = '';
    this.filtrar('');

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { busca: null },
      queryParamsHandling: 'merge'
    });
  }

  /** Recarrega lista de árvores */
  carregarArvores(): void {
    this.refresh$.next();
  }

  /**
   * Inicializa formulário com valores padrão
   */
  private inicializarFormulario(): ArvoreRequest {
    return {
      nomePopular: '',
      nomeCientifico: '',
      taxaAbsorcaoCo2Anual: 0,
      custoMedioMuda: 0,
      tempoMaturidadeAnos: 0,
      alturaMediaM: 0,
      diametroCopaMedioM: 0,
      biomasIds: [],
      climasIds: []
    };
  }

  /**
   * Bloqueia entrada de caracteres não numéricos
   */
  bloquearTeclasNaoNumericas(event: KeyboardEvent): void {
    const allowed = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter', '.', ','];
    if (allowed.includes(event.key) || (event.ctrlKey || event.metaKey)) return;
    if (!/^[0-9]$/.test(event.key)) event.preventDefault();
  }

  /** Valida se formulário está preenchido corretamente */
  get formularioValido(): boolean {
    const a = this.novaArvore;
    return (
      a.nomePopular.trim().length > 0 &&
      a.nomeCientifico.trim().length > 0 &&
      a.taxaAbsorcaoCo2Anual > 0 &&
      a.biomasIds.length > 0 &&
      a.climasIds.length > 0
    );
  }

  /** Abre modal para cadastro de nova árvore */
  abrirModalCadastro(): void {
    this.novaArvore = this.inicializarFormulario();
    this.arvoreEmEdicaoId = null;
    (document.getElementById('modal_arvore') as any).showModal();
  }

  /**
   * Prepara formulário para edição de árvore existente
   */
  prepararEdicao(arvore: ArvoreResponse): void {
    this.arvoreEmEdicaoId = arvore.id;

    this.novaArvore = {
      nomePopular: arvore.nomePopular,
      nomeCientifico: arvore.nomeCientifico,
      taxaAbsorcaoCo2Anual: arvore.taxaAbsorcaoCo2Anual,
      custoMedioMuda: arvore.custoMedioMuda,
      tempoMaturidadeAnos: arvore.tempoMaturidadeAnos,
      alturaMediaM: arvore.alturaMediaM,
      diametroCopaMedioM: arvore.diametroCopaMedioM,
      biomasIds: arvore.biomas.map(b => b.id),
      climasIds: arvore.climas.map(c => c.id)
    };

    (document.getElementById('modal_arvore') as any).showModal();
  }

  /** Salva árvore (criação ou edição) */
  salvarArvore(): void {
    if (!this.formularioValido) return;

    const payload: ArvoreRequest = {
      ...this.novaArvore,
      nomePopular: this.novaArvore.nomePopular.trim(),
      nomeCientifico: this.novaArvore.nomeCientifico.trim()
    };

    const request$ = this.arvoreEmEdicaoId
      ? this.arvoreService.atualizar(this.arvoreEmEdicaoId, payload)
      : this.arvoreService.criar(payload);

    request$.subscribe({
      next: () => {
        this.carregarArvores();
        (document.getElementById('modal_arvore') as any).close();
      },
      error: (err) => alert('Erro: ' + (err.error?.message || 'Falha ao salvar.'))
    });
  }

  /** Remove árvore após confirmação */
  deletarArvore(id: string): void {
    if(confirm('Tem certeza que deseja excluir esta espécie?')) {
      this.arvoreService.deletar(id).subscribe(() => this.carregarArvores());
    }
  }
}
