import { createRouter, createWebHistory } from 'vue-router'


import Auth from '@/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/AboutView.vue'),
    },
    {
      path: '/auth', // Redirect URI
      name: 'Authenticated',
      component: () => import('@/views/AuthView.vue')
    },
    {
      path: '/silent-refresh', // Token silent refresh URI
      name: 'SilentRefresh',
      component: () => import('@/views/SilentRefreshView.vue')
    },
    {
      path: '/user',
      name: 'User',
      component:() => import('@/views/User.vue'),
      //meta:{ requiresAuth: true }
    }
    // { // Example of requiring auth on a page.
    //   path: '/auth-required',
    //   name: 'AuthRequired',
    //   component: AuthRequired,
    //   meta: { requiresAuth: true }
    // },
  ],
})

router.beforeEach(async (to, from, next) => {
  const hasAuth = await Auth.getUser()
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!hasAuth) {
      return next({
        path: '/unauthenticated'
      })
    }
  }
  return next()
})


export default router
