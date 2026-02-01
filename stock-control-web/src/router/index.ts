import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/components/Layout.vue'),
      children: [
        { path: '', redirect: '/products' },
        { path: 'products', name: 'Products', component: () => import('@/pages/ProductsPage.vue') },
        { path: 'stock-movement', name: 'StockMovement', component: () => import('@/pages/StockMovementPage.vue') },
        { path: 'reports/stock', name: 'ReportStock', component: () => import('@/pages/ReportStockPage.vue') },
        { path: 'reports/profit', name: 'ReportProfit', component: () => import('@/pages/ReportProfitPage.vue') },
      ],
    },
  ],
})

export default router
