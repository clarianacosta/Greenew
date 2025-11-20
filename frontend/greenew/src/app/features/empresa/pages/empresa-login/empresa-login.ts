import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

import { EmpresaService } from '../../../../core/services/empresa.service';
import { EmpresaRequest, EmpresaResponse } from '../../../../core/models/models';
import { LogoSimple } from '../../../../shared/logo/logo-simple/logo-simple';

@Component({
  selector: 'app-empresa-login',
  standalone: true,
  imports: [CommonModule, FormsModule, LogoSimple],
  templateUrl: './empresa-login.html'
})
export class EmpresaLogin {
  private empresaService = inject(EmpresaService);
  private router = inject(Router);

  modo: 'LOGIN' | 'CADASTRO' = 'LOGIN';
  empresaSelecionadaId: string = '';

  empresas$: Observable<EmpresaResponse[]> = this.empresaService.listarTodas();

  novaEmpresa: EmpresaRequest = { razaoSocial: '', cnpj: '' };

  alternarModo(novoModo: 'LOGIN' | 'CADASTRO'): void {
    this.modo = novoModo;
  }

  entrar(): void {
    if (!this.empresaSelecionadaId) return;
    localStorage.setItem('empresa_logada_id', this.empresaSelecionadaId);
    this.router.navigate(['/empresa/dashboard']);
  }

  cadastrar(): void {
    // Validação simples antes de enviar
    if (!this.novaEmpresa.razaoSocial || this.novaEmpresa.cnpj.length < 18) {
        alert('Preencha os dados corretamente.');
        return;
    }

    // Remove formatação para enviar ao backend (apenas números)
    const payload = {
        ...this.novaEmpresa,
        cnpj: this.novaEmpresa.cnpj.replace(/\D/g, '')
    };

    this.empresaService.criar(payload).subscribe({
      next: (empresa) => {
        localStorage.setItem('empresa_logada_id', empresa.id);
        this.router.navigate(['/empresa/dashboard']);
      },
      error: (err) => alert('Erro ao cadastrar: ' + (err.error?.message || err.message))
    });
  }

  // --- MÁSCARAS E VALIDAÇÕES ---

  bloquearTeclasNaoNumericas(event: KeyboardEvent): void {
    const teclasPermitidas = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter'];
    if (teclasPermitidas.includes(event.key) || (event.ctrlKey || event.metaKey)) return;
    if (!/^[0-9]$/.test(event.key)) event.preventDefault();
  }

  formatarCNPJ(valor: string): void {
    let v = valor.replace(/\D/g, '');
    if (v.length > 14) v = v.substring(0, 14);

    v = v.replace(/^(\d{2})(\d)/, '$1.$2');
    v = v.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
    v = v.replace(/\.(\d{3})(\d)/, '.$1/$2');
    v = v.replace(/(\d{4})(\d)/, '$1-$2');

    this.novaEmpresa.cnpj = v;
  }
}
