import { Routes } from '@angular/router';
import { LandingPage } from './features/home/page/landing-page/landing-page';
import { AdminDashboard } from './features/admin/pages/admin-dashboard/admin-dashboard';
import { AdminEmpresas } from './features/admin/pages/admin-empresas/admin-empresas';
import { AdminArvores } from './features/admin/pages/admin-arvores/admin-arvores';
import { AdminProdutores } from './features/admin/pages/admin-produtores/admin-produtores';
import { AdminTerrenos } from './features/admin/pages/admin-terrenos/admin-terrenos';

// Importe seus componentes aqui (você precisará criá-los)
// import { AdminDashboardComponent } ...
// import { CompanySelectionComponent } ...
// import { ReportListComponent } ...
// import { ReportWizardComponent } ...

export const routes: Routes = [
  // Rota Pública
  { path: '', component: LandingPage },

  // "Login" Simulado
  // { path: 'acesso', component: CompanySelectionComponent }, // Componente para escolher a empresa

  // Área do Cliente (Fluxo Principal)
  {
    path: 'app',
    children: [
      // { path: 'relatorios', component: ReportListComponent },       // Lista relatórios da empresa
      // { path: 'relatorios/:id', component: ReportWizardComponent }  // O "Coração" do sistema (Passos 1 a 4)
    ]
  },

  // Área Administrativa (Cadastros Base)
  {
    path: 'admin',
    children: [
      { path: '', component: AdminDashboard },
      { path: 'empresas', component: AdminEmpresas },
      { path: 'arvores', component: AdminArvores },
      { path: 'produtores', component: AdminProdutores },
      { path: 'terrenos', component: AdminTerrenos }
    ]
  },

  { path: '**', redirectTo: '' }
];
