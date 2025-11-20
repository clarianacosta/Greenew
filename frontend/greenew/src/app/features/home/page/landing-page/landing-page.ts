import { Component } from '@angular/core';
import { ClearNavbar } from '../../../../shared/clear-navbar/clear-navbar';
import { HeaderDivider } from '../../../home/components/header-divider/header-divider';
import { LogoSimple } from '../../../../shared/logo/logo-simple/logo-simple';

/**
 * Componente da página inicial do sistema Greenew
 * Apresenta a landing page com navegação limpa e elementos visuais principais
 */
@Component({
  selector: 'app-landing-page',
  imports: [ClearNavbar, HeaderDivider, LogoSimple],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.scss',
})
export class LandingPage {}
