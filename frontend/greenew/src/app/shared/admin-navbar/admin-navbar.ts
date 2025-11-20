import { Component } from '@angular/core';
import { LogoSimple } from '../logo/logo-simple/logo-simple';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-navbar',
  imports: [LogoSimple, RouterModule],
  templateUrl: './admin-navbar.html',
  styleUrl: './admin-navbar.scss'
})
export class AdminNavbar {

}
