import { Routes } from '@angular/router';
import { AppLayout } from './layout/app-layout/app-layout';
import { SalonListPage } from './features/salons/pages/salon-list-page/salon-list-page';

export const routes: Routes = [
  {
    path: '',
    component: AppLayout,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'salons',
      },
      {
        path: 'salons',
        component: SalonListPage,
      },
    ],
  },
  {
    path: '**',
    redirectTo: 'salons',
  },
];
