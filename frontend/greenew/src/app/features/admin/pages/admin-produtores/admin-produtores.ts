import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, combineLatest, map, switchMap } from 'rxjs';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { ProdutorService } from '../../../../core/services/produtor.service';
import { ProdutorResponse, ProdutorRequest } from '../../../../core/models/models';

/**
 * Componente de administração de produtores
 * Gerencia CRUD de produtores com busca por nome ou email
 */
@Component({
  selector: 'app-admin-produtores',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AdminNavbar],
  templateUrl: './admin-produtores.html'
})
export class AdminProdutores implements OnInit {
  /** Serviços injetados */
  private produtorService = inject(ProdutorService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  /** Controle de estado */
  private refresh$ = new BehaviorSubject<void>(undefined);
  private termoBusca$ = new BehaviorSubject<string>('');

  /** Interface de busca */
  termoInput: string = '';

  /** Observable de dados */
  produtores$: Observable<ProdutorResponse[]>;

  /** Formulário de produtor */
  novoProdutor: ProdutorRequest = { nomeCompleto: '', email: '', celular: '' };
  produtorEmEdicaoId: string | null = null;

  constructor() {
    this.produtores$ = combineLatest([
      this.refresh$.pipe(switchMap(() => this.produtorService.listarTodos())),
      this.termoBusca$
    ]).pipe(
      map(([lista, termo]) => {
        if (!termo || termo.trim() === '') {
          return lista;
        }

        const termoLower = termo.toLowerCase();
        return lista.filter(p =>
          p.nomeCompleto.toLowerCase().includes(termoLower) ||
          p.email.toLowerCase().includes(termoLower)
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

  /** Recarrega lista de produtores */
  carregarProdutores(): void {
    this.refresh$.next();
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
    const teclasPermitidas = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter'];
    if (teclasPermitidas.includes(event.key) || (event.ctrlKey || event.metaKey)) return;
    if (!/^[0-9]$/.test(event.key)) event.preventDefault();
  }

  /**
   * Aplica máscara de formatação no celular
   */
  formatarCelular(valor: string): void {
    let v = valor.replace(/\D/g, '');
    if (v.length > 11) v = v.substring(0, 11);

    if (v.length > 10) {
      v = v.replace(/^(\d\d)(\d{5})(\d{4}).*/, '($1) $2-$3');
    } else if (v.length > 5) {
      v = v.replace(/^(\d\d)(\d{4})(\d{0,4}).*/, '($1) $2-$3');
    } else if (v.length > 2) {
      v = v.replace(/^(\d\d)(\d{0,5})/, '($1) $2');
    }
    this.novoProdutor.celular = v;
  }

  /**
   * Verifica se texto contém padrões suspeitos de SQL injection
   */
  contemSqlInjection(texto: string): boolean {
    if (!texto) return false;
    const padraoSuspeito = /('|"|;|--|\/\*|\*\/|drop table|update |delete |insert |select |alter )/i;
    return padraoSuspeito.test(texto);
  }

  /**
   * Valida formato de email
   */
  validarEmail(email: string): boolean {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
  }

  /** Valida se formulário está preenchido corretamente */
  get formularioValido(): boolean {
    const nome = this.novoProdutor.nomeCompleto.trim();
    const email = this.novoProdutor.email.trim();
    const celularLimpo = (this.novoProdutor.celular || '').replace(/\D/g, '');

    const nomeValido = nome.length > 2 && nome.length <= 100 && !this.contemSqlInjection(nome);
    const emailValido = this.validarEmail(email) && !this.contemSqlInjection(email);
    const celularValido = celularLimpo.length === 0 || (celularLimpo.length >= 10 && celularLimpo.length <= 11);

    return nomeValido && emailValido && celularValido;
  }

  /** Abre modal para cadastro de novo produtor */
  abrirModalCadastro(): void {
    this.novoProdutor = { nomeCompleto: '', email: '', celular: '' };
    this.produtorEmEdicaoId = null;
    (document.getElementById('modal_produtor') as any).showModal();
  }

  /**
   * Prepara formulário para edição de produtor existente
   */
  prepararEdicao(produtor: ProdutorResponse): void {
    this.produtorEmEdicaoId = produtor.id;
    this.novoProdutor = {
      nomeCompleto: produtor.nomeCompleto,
      email: produtor.email,
      celular: produtor.celular || ''
    };
    if (this.novoProdutor.celular) this.formatarCelular(this.novoProdutor.celular);
    (document.getElementById('modal_produtor') as any).showModal();
  }

  /** Salva produtor (criação ou edição) */
  salvarProdutor(): void {
    if (!this.formularioValido) return;

    const payload: ProdutorRequest = {
      nomeCompleto: this.novoProdutor.nomeCompleto.trim(),
      email: this.novoProdutor.email.trim(),
      celular: this.novoProdutor.celular ? this.novoProdutor.celular.replace(/\D/g, '') : undefined
    };

    const request$ = this.produtorEmEdicaoId
      ? this.produtorService.atualizar(this.produtorEmEdicaoId, payload)
      : this.produtorService.criar(payload);

    request$.subscribe({
      next: () => {
        this.carregarProdutores();
        (document.getElementById('modal_produtor') as any).close();
      },
      error: (err) => alert('Erro: ' + (err.error?.message || 'Falha na operação.'))
    });
  }

  /** Remove produtor após confirmação */
  deletarProdutor(id: string): void {
    if(confirm('Tem certeza que deseja excluir?')) {
      this.produtorService.deletar(id).subscribe({
        next: () => this.carregarProdutores(),
        error: () => alert('Não foi possível excluir. Verifique se existem terrenos vinculados.')
      });
    }
  }
}
