import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { BehaviorSubject, Observable, combineLatest, map, switchMap } from 'rxjs';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { TerrenoService } from '../../../../core/services/terreno.service';
import { ProdutorService } from '../../../../core/services/produtor.service';
import { ArvoreService } from '../../../../core/services/arvore.service';
import {
  TerrenoResponse,
  TerrenoRequest,
  ProdutorResponse,
  Bioma,
  Clima,
} from '../../../../core/models/models';

/**
 * Componente de administração de terrenos
 * Gerencia CRUD de terrenos com filtros por produtor e visualização em mapa
 */
@Component({
  selector: 'app-admin-terrenos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AdminNavbar],
  templateUrl: './admin-terrenos.html',
})
export class AdminTerrenos implements OnInit {
  /** Serviços injetados */
  private terrenoService = inject(TerrenoService);
  private produtorService = inject(ProdutorService);
  private arvoreService = inject(ArvoreService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private sanitizer = inject(DomSanitizer);

  /** Controle de estado e observables */
  private refresh$ = new BehaviorSubject<void>(undefined);
  
  terrenos$: Observable<TerrenoResponse[]>;
  produtores$: Observable<ProdutorResponse[]>;
  biomas$: Observable<Bioma[]>;
  climas$: Observable<Clima[]>;

  /** Controle de filtros */
  filtroProdutorId: string | null = null;
  produtorFiltradoNome: string = '';

  /** Mapa seguro para iframe */
  urlMapaSegura: SafeResourceUrl | null = null;

  /** Formulário de terreno */
  novoTerreno: TerrenoRequest = this.inicializarFormulario();
  terrenoEmEdicaoId: string | null = null;

  constructor() {
    this.produtores$ = this.produtorService.listarTodos();
    this.biomas$ = this.arvoreService.listarBiomas();
    this.climas$ = this.arvoreService.listarClimas();

    this.terrenos$ = combineLatest([
      this.refresh$.pipe(switchMap(() => this.terrenoService.listarTodos())),
      this.route.queryParams,
    ]).pipe(
      map(([terrenos, params]) => {
        const pId = params['produtorId'];

        if (pId) {
          this.filtroProdutorId = pId;
          const t = terrenos.find((x) => x.produtor.id === pId);
          this.produtorFiltradoNome = t ? t.produtor.nomeCompleto : 'Produtor Selecionado';
          return terrenos.filter((x) => x.produtor.id === pId);
        } else {
          this.filtroProdutorId = null;
          this.produtorFiltradoNome = '';
          return terrenos;
        }
      })
    );
  }

  ngOnInit(): void {
    this.verificarDeeplink();
  }

  /**
   * Verifica se há parâmetros de deeplink para abrir modal automaticamente
   */
  private verificarDeeplink(): void {
    this.route.queryParams.subscribe((params: any) => {
      if (params['openModal'] === 'true' && params['produtorId']) {
        this.filtroProdutorId = params['produtorId'];
        setTimeout(() => {
          this.abrirModalCadastro();
        }, 100);
      }
    });
  }

  /** Recarrega dados dos terrenos */
  carregarDados(): void {
    this.refresh$.next();
  }

  /** Remove filtro de produtor */
  limparFiltro(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { produtorId: null, openModal: null },
      queryParamsHandling: 'merge',
    });
  }

  /**
   * Inicializa formulário com valores padrão
   */
  private inicializarFormulario(): TerrenoRequest {
    return {
      latitude: 0,
      longitude: 0,
      areaDisponivelHectares: 0,
      biomaIdLocal: '',
      climaIdLocal: '',
      produtorId: '',
    };
  }

  /**
   * Gera iniciais do nome para avatar
   */
  obterIniciais(nome: string): string {
    if (!nome) return '';
    const partes = nome.trim().split(/\s+/);
    if (partes.length === 1) {
      return partes[0].substring(0, 2).toUpperCase();
    }
    const inicialPrimeiro = partes[0][0];
    const inicialUltimo = partes[partes.length - 1][0];
    return (inicialPrimeiro + inicialUltimo).toUpperCase();
  }

  /**
   * Bloqueia entrada de caracteres não numéricos
   */
  bloquearTeclasNaoNumericas(event: KeyboardEvent): void {
    const allowed = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter', '.', ',', '-'];
    if (allowed.includes(event.key) || event.ctrlKey || event.metaKey) return;
    if (!/^[0-9]$/.test(event.key)) event.preventDefault();
  }

  /** Valida se formulário está preenchido corretamente */
  get formularioValido(): boolean {
    const t = this.novoTerreno;
    return !!(
      t.produtorId && t.biomaIdLocal && t.climaIdLocal &&
      t.areaDisponivelHectares > 0 && t.latitude && t.longitude
    );
  }

  /**
   * Abre modal com mapa do terreno
   */
  abrirMapa(terreno: TerrenoResponse): void {
    if (!terreno.latitude || !terreno.longitude) return;

    const url = `https://maps.google.com/maps?q=${terreno.latitude},${terreno.longitude}&hl=pt&z=15&output=embed`;
    this.urlMapaSegura = this.sanitizer.bypassSecurityTrustResourceUrl(url);

    (document.getElementById('modal_mapa') as any).showModal();
  }

  /** Abre modal para cadastro de novo terreno */
  abrirModalCadastro(): void {
    this.novoTerreno = this.inicializarFormulario();
    this.terrenoEmEdicaoId = null;

    if (this.filtroProdutorId) {
      this.novoTerreno.produtorId = this.filtroProdutorId;
    }

    const modal = document.getElementById('modal_terreno') as any;
    if (modal) modal.showModal();
  }

  /**
   * Prepara formulário para edição de terreno existente
   */
  prepararEdicao(terreno: TerrenoResponse): void {
    this.terrenoEmEdicaoId = terreno.id;
    this.novoTerreno = {
      produtorId: terreno.produtor.id,
      biomaIdLocal: terreno.biomaLocal.id,
      climaIdLocal: terreno.climaLocal.id,
      latitude: terreno.latitude,
      longitude: terreno.longitude,
      areaDisponivelHectares: terreno.areaDisponivelHectares,
    };
    (document.getElementById('modal_terreno') as any).showModal();
  }

  /** Salva terreno (criação ou edição) */
  salvarTerreno(): void {
    if (!this.formularioValido) return;

    const request$ = this.terrenoEmEdicaoId
      ? this.terrenoService.atualizar(this.terrenoEmEdicaoId, this.novoTerreno)
      : this.terrenoService.criar(this.novoTerreno);

    request$.subscribe({
      next: () => {
        this.carregarDados();
        (document.getElementById('modal_terreno') as any).close();
      },
      error: (err) => alert('Erro: ' + (err.error?.message || 'Erro ao salvar.')),
    });
  }

  /** Remove terreno após confirmação */
  deletarTerreno(id: string): void {
    if (confirm('Excluir terreno permanentemente?')) {
      this.terrenoService.deletar(id).subscribe(() => this.carregarDados());
    }
  }
}
