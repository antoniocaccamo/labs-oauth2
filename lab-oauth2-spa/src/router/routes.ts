import type { RouteRecordRaw } from 'vue-router';
import ErrorComponent from 'src/components/ErrorComponent.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') },
      { path: '/books', component: () => import('pages/BooksPage.vue'), meta: { authenticated: true }, }
    ],
  },
  {
    path: '/auth/callback:state(.*)', // Redirect URI
    name: 'AuthCallback',
    component: () => import('pages/AuthCallbackView.vue')
  },

  {
    path: '/code(.*)', // Redirect URI
    name: 'Code',
    component: () => import('pages/AuthCallbackView.vue')
  },
  {
    path: '/silent-refresh', // Token silent refresh URI
    name: 'SilentRefresh',
    component: () => import('pages/SilentRefreshView.vue')
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
  {
    path: '/forbidden',
    name: 'Forbidden',
    component: ErrorComponent,
    meta: { error403: true },
  },
];

export default routes;
