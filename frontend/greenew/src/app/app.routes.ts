import { Routes } from '@angular/router';
import { LandingPage } from './features/home/page/landing-page/landing-page';
import { AdminDashboard } from './features/admin/pages/admin-dashboard/admin-dashboard';
import { AdminEmpresas } from './features/admin/pages/admin-empresas/admin-empresas';
import { AdminArvores } from './features/admin/pages/admin-arvores/admin-arvores';
import { AdminProdutores } from './features/admin/pages/admin-produtores/admin-produtores';
import { AdminTerrenos } from './features/admin/pages/admin-terrenos/admin-terrenos';
import { EmpresaDashboard } from './features/empresa/pages/empresa-dashboard/empresa-dashboard';
import { EmpresaLogin } from './features/empresa/pages/empresa-login/empresa-login';
import { NovoRelatorio } from './features/empresa/pages/novo-relatorio/novo-relatorio';
import { AdminFatores } from './features/admin/pages/admin-fatores/admin-fatores';

export const routes: Routes = [
  { path: '', component: LandingPage },

  {
    path: 'admin',
    children: [
      { path: '', component: AdminDashboard },
      { path: 'empresas', component: AdminEmpresas },
      { path: 'arvores', component: AdminArvores },
      { path: 'produtores', component: AdminProdutores },
      { path: 'terrenos', component: AdminTerrenos },
      { path: 'fatores', component: AdminFatores }
    ]
  },

  {
    path: 'empresa',
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      },
      {
        path: 'login',
        component: EmpresaLogin
      },
      {
        path: 'dashboard',
        component: EmpresaDashboard
      },
      {
        path: 'novo-relatorio',
        component: NovoRelatorio
      }
    ]
  },

  { path: '**', redirectTo: '' }
];
