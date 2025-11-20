import { Component } from '@angular/core';
import { LogoSimple } from '../logo/logo-simple/logo-simple';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-empresa-navbar',
  imports: [LogoSimple, RouterModule],
  templateUrl: './empresa-navbar.html',
  styleUrl: './empresa-navbar.scss'
})
export class EmpresaNavbar {

}
