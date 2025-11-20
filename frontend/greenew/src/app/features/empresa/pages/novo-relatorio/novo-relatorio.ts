import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, of, from } from 'rxjs';
import { concatMap, toArray, finalize } from 'rxjs/operators';

import { EmpresaNavbar } from '../../../../shared/empresa-navbar/empresa-navbar';
import { RelatorioService } from '../../../../core/services/relatorio.service';
import {
  FatorEmissao,
  AtividadeResponse,
  RelatorioResponse,
  RelatorioCalculado,
  RecomendacaoRanqueada
} from '../../../../core/models/models';

interface ItemInventario {
  fatorEmissaoId: string;
  quantidade: number;
  unidade?: string;
}

@Component({
  selector: 'app-novo-relatorio',
  standalone: true,
  imports: [CommonModule, FormsModule, EmpresaNavbar],
  templateUrl: './novo-relatorio.html'
})
export class NovoRelatorio implements OnInit {
  private relatorioService = inject(RelatorioService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  // --- ESTADO ---
  passoAtual = 1;
  loading = false;
  empresaId = localStorage.getItem('empresa_logada_id');
  modoEdicao = false;

  // --- DADOS ---
  anoReferencia: number = new Date().getFullYear();
  relatorioCriado: RelatorioResponse | null = null;

  listaFatores: FatorEmissao[] = [];
  itensInventario: ItemInventario[] = [];
  atividades$: Observable<AtividadeResponse[]> = of([]);

  resultadoCalculo: RelatorioCalculado | null = null;

  recomendacoes$: Observable<RecomendacaoRanqueada[]> = of([]);
  recomendacaoEscolhidaId: string | null = null;
  expandedRecomendacaoId: string | null = null;

  constructor() {
    this.relatorioService.listarFatoresEmissao().subscribe(f => this.listaFatores = f);
  }

ngOnInit(): void {
    if (!this.empresaId) {
      this.router.navigate(['/empresa/login']);
      return;
    }

    // Verifica ID na rota
    const idEdicao = this.route.snapshot.paramMap.get('id');

    if (idEdicao) {
      this.modoEdicao = true; // Ativa o modo de edição
      this.carregarRelatorioExistente(idEdicao);
    } else {
      this.modoEdicao = false;
      this.adicionarLinha();
    }
  }

  // --- NAVEGAÇÃO ---
  voltar(): void {
    if (this.passoAtual > 1) this.passoAtual--;
  }

  // --- CARREGAMENTO (EDIÇÃO) ---
  carregarRelatorioExistente(id: string): void {
    this.loading = true;
    this.relatorioService.buscarPorId(id).subscribe({
      next: (relatorio) => {
        this.relatorioCriado = relatorio;
        this.anoReferencia = relatorio.anoReferencia;

        // Se já tiver cálculo, pula para o resultado
        if (relatorio.nivel === 'COMPLETO' || (relatorio.emissaoCalculadaCo2e && relatorio.emissaoCalculadaCo2e > 0)) {
          this.resultadoCalculo = {
            id: relatorio.id,
            anoReferencia: relatorio.anoReferencia,
            emissaoCalculadaCo2e: relatorio.emissaoCalculadaCo2e,
            nivel: relatorio.nivel
          };
          this.passoAtual = 3;
        } else {
          // Se for rascunho, vai para o inventário
          this.passoAtual = 2;
          // Opcional: Aqui você poderia carregar as atividades já salvas do backend para preencher o grid
          this.adicionarLinha();
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        alert('Erro ao carregar relatório: ' + err.message);
        this.router.navigate(['/empresa/dashboard']);
      }
    });
  }

  // --- PASSO 1: INICIAR ---
  iniciarRelatorio(): void {
    if (!this.empresaId) return;
    this.loading = true;

    this.relatorioService.criar({
      empresaId: this.empresaId,
      anoReferencia: this.anoReferencia
    }).subscribe({
      next: (res) => {
        this.relatorioCriado = res;
        this.loading = false;
        this.passoAtual = 2;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        alert('Erro ao criar: ' + err.message);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // --- PASSO 2: INVENTÁRIO ---
  adicionarLinha(): void {
    this.itensInventario.push({ fatorEmissaoId: '', quantidade: 0, unidade: '' });
  }

  removerLinha(index: number): void {
    if (this.itensInventario.length > 1) {
      this.itensInventario.splice(index, 1);
    } else {
      this.itensInventario[0] = { fatorEmissaoId: '', quantidade: 0, unidade: '' };
    }
  }

  aoSelecionarFator(item: ItemInventario): void {
    const fator = this.listaFatores.find(f => f.id === item.fatorEmissaoId);
    if (fator) item.unidade = fator.unidade;
  }

  get formularioInventarioValido(): boolean {
    return this.itensInventario.some(i => i.fatorEmissaoId && i.quantidade > 0);
  }

  salvarInventarioECalcular(): void {
    if (!this.relatorioCriado || !this.formularioInventarioValido) return;
    this.loading = true;

    const itensValidos = this.itensInventario.filter(i => i.fatorEmissaoId && i.quantidade > 0);

    // Salva um por um e depois calcula
    from(itensValidos).pipe(
      concatMap(item =>
        this.relatorioService.adicionarAtividade(this.relatorioCriado!.id, {
          fatorEmissaoId: item.fatorEmissaoId,
          quantidade: item.quantidade
        })
      ),
      toArray(),
      finalize(() => this.calcularEmissoes())
    ).subscribe({
      error: (err: any) => {
        console.error(err);
        alert('Erro ao salvar atividades. Verifique o console.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // --- PASSO 3: CÁLCULO ---
  calcularEmissoes(): void {
    if (!this.relatorioCriado) return;

    this.relatorioService.calcularEmissoes(this.relatorioCriado.id, [1, 2, 3]).subscribe({
      next: (res) => {
        this.resultadoCalculo = res;
        this.loading = false;
        this.passoAtual = 3;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        alert('Erro no cálculo: ' + err.message);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  irParaCompensacao(): void {
    if (!this.relatorioCriado) return;
    this.passoAtual = 4;
    this.recomendacoes$ = this.relatorioService.buscarRecomendacoes(this.relatorioCriado.id);
  }

  // --- PASSO 4: COMPENSAÇÃO ---
  selecionarRecomendacao(arvoreId: string): void {
    this.recomendacaoEscolhidaId = arvoreId;
  }

  toggleExpand(event: Event, arvoreId: string): void {
    event.stopPropagation();
    if (this.expandedRecomendacaoId === arvoreId) {
      this.expandedRecomendacaoId = null;
    } else {
      this.expandedRecomendacaoId = arvoreId;
    }
  }

  finalizar(): void {
    if (!this.relatorioCriado || !this.recomendacaoEscolhidaId) return;
    this.loading = true;

    this.relatorioService.atribuirRecomendacao(this.relatorioCriado.id, this.recomendacaoEscolhidaId)
      .subscribe({
        next: () => {
          alert('Sucesso! Relatório finalizado.');
          this.router.navigate(['/empresa/dashboard']);
        },
        error: (err: any) => {
          alert('Erro: ' + err.message);
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }
}
