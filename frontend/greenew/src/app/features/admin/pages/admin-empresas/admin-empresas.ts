import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, combineLatest, map, switchMap } from 'rxjs';
import { AdminNavbar } from '../../../../shared/admin-navbar/admin-navbar';
import { EmpresaService } from '../../../../core/services/empresa.service';
import { EmpresaResponse, EmpresaRequest } from '../../../../core/models/models';

/**
 * Componente de administração de empresas
 * Gerencia CRUD de empresas com busca por razão social ou CNPJ
 */
@Component({
  selector: 'app-admin-empresas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AdminNavbar],
  templateUrl: './admin-empresas.html'
})
export class AdminEmpresas implements OnInit {
  /** Serviços injetados */
  private empresaService = inject(EmpresaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  /** Controle de estado */
  private refresh$ = new BehaviorSubject<void>(undefined);
  private termoBusca$ = new BehaviorSubject<string>('');

  /** Interface de busca */
  termoInput: string = '';

  /** Observable de dados */
  empresas$: Observable<EmpresaResponse[]>;

  /** Formulário de empresa */
  novaEmpresa: EmpresaRequest = { razaoSocial: '', cnpj: '' };
  empresaEmEdicaoId: string | null = null;

  constructor() {
    this.empresas$ = combineLatest([
      this.refresh$.pipe(switchMap(() => this.empresaService.listarTodas())),
      this.termoBusca$
    ]).pipe(
      map(([lista, termo]) => {
        if (!termo || termo.trim() === '') {
          return lista;
        }

        const termoLower = termo.toLowerCase();
        const termoNumerico = termo.replace(/\D/g, '');

        return lista.filter(e => {
          const matchNome = e.razaoSocial.toLowerCase().includes(termoLower);
          const matchCnpj = e.cnpj.includes(termoLower) ||
                            (termoNumerico.length > 0 && e.cnpj.replace(/\D/g, '').includes(termoNumerico));

          return matchNome || matchCnpj;
        });
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

  /** Recarrega lista de empresas */
  carregarEmpresas(): void {
    this.refresh$.next();
  }

  /**
   * Bloqueia entrada de caracteres não numéricos
   */
  bloquearTeclasNaoNumericas(event: KeyboardEvent): void {
    const teclasPermitidas = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter'];
    if (teclasPermitidas.includes(event.key) ||
       (event.ctrlKey || event.metaKey) && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase())) {
      return;
    }
    if (!/^[0-9]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  /**
   * Aplica máscara de formatação no CNPJ
   */
  formatarCNPJ(valor: string): void {
    let v = valor.replace(/\D/g, '');
    if (v.length > 14) v = v.substring(0, 14);
    v = v.replace(/^(\d{2})(\d)/, '$1.$2');
    v = v.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
    v = v.replace(/\.(\d{3})(\d)/, '.$1/$2');
    v = v.replace(/(\d{4})(\d)/, '$1-$2');
    this.novaEmpresa.cnpj = v;
  }

  /**
   * Verifica se texto contém padrões suspeitos de SQL injection
   */
  contemSqlInjection(texto: string): boolean {
    if (!texto) return false;
    const padraoSuspeito = /('|"|;|--|\/\*|\*\/|drop table|update |delete |insert |select |alter )/i;
    return padraoSuspeito.test(texto);
  }

  /** Valida se formulário está preenchido corretamente */
  get formularioValido(): boolean {
    const cnpjLimpo = this.novaEmpresa.cnpj.replace(/\D/g, '');
    const razao = this.novaEmpresa.razaoSocial.trim();
    const cnpjValido = cnpjLimpo.length === 14;
    const razaoValida = razao.length > 0 && razao.length <= 100 && !this.contemSqlInjection(razao);
    return razaoValida && cnpjValido;
  }

  /** Abre modal para cadastro de nova empresa */
  abrirModalCadastro(): void {
    this.novaEmpresa = { razaoSocial: '', cnpj: '' };
    this.empresaEmEdicaoId = null;
    (document.getElementById('modal_empresa') as any).showModal();
  }

  /**
   * Prepara formulário para edição de empresa existente
   */
  prepararEdicao(empresa: EmpresaResponse): void {
    this.empresaEmEdicaoId = empresa.id;
    this.novaEmpresa = { razaoSocial: empresa.razaoSocial, cnpj: '' };
    this.formatarCNPJ(empresa.cnpj);
    (document.getElementById('modal_empresa') as any).showModal();
  }

  /** Salva empresa (criação ou edição) */
  salvarEmpresa(): void {
    if (!this.formularioValido) return;

    const payload: EmpresaRequest = {
      razaoSocial: this.novaEmpresa.razaoSocial.trim(),
      cnpj: this.novaEmpresa.cnpj.replace(/\D/g, '')
    };

    const request$ = this.empresaEmEdicaoId
      ? this.empresaService.atualizar(this.empresaEmEdicaoId, payload)
      : this.empresaService.criar(payload);

    request$.subscribe({
      next: () => {
        this.carregarEmpresas();
        (document.getElementById('modal_empresa') as any).close();
      },
      error: (err) => alert('Erro: ' + (err.error?.message || 'Falha na operação.'))
    });
  }

  /** Remove empresa após confirmação */
  deletarEmpresa(id: string): void {
    if(confirm('Tem certeza que deseja excluir?')) {
      this.empresaService.deletar(id).subscribe(() => this.carregarEmpresas());
    }
  }
}
