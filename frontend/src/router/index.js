import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/users',
      component: () => import('../components/ui/UsersGrid.vue'),
    },
    {
      path: '/drafts',
      component: () => import('../components/ui/DraftsGrid.vue'),
    },
    {
      path: '/checkAuthors',
      component: () => import('../components/CheckAuthorsView.vue'),
    },
    {
      path: '/products',
      component: () => import('../components/ui/ProductsGrid.vue'),
    },
    {
      path: '/userAccessProfiles',
      component: () => import('../components/ui/UserAccessProfileGrid.vue'),
    },
    {
      path: '/checkPrices',
      component: () => import('../components/CheckPriceView.vue'),
    },
    {
      path: '/checkIfBoughts',
      component: () => import('../components/CheckIfBoughtView.vue'),
    },
    {
      path: '/viewHistories',
      component: () => import('../components/ViewHistoryView.vue'),
    },
  ],
})

export default router;
