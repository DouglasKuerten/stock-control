<script setup lang="ts">
import { RouterLink } from 'vue-router'

const navItems = [
  { to: '/products', label: 'Produtos', icon: 'products' },
  { to: '/stock-movement', label: 'Movimentações', icon: 'movement' },
  { to: '/reports/stock', label: 'Estoque', icon: 'chart' },
  { to: '/reports/profit', label: 'Lucro', icon: 'profit' },
]
</script>

<template>
  <header class="border-b border-surface-200 bg-white shadow-card">
    <div class="mx-auto flex max-w-6xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
      <RouterLink to="/" class="flex items-center gap-2">
        <div class="flex h-9 w-9 items-center justify-center rounded-lg bg-primary-600">
          <svg class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
          </svg>
        </div>
        <span class="text-lg font-semibold text-gray-900">Stock Control</span>
      </RouterLink>
      <nav class="flex items-center gap-1">
        <RouterLink
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          class="rounded-lg px-3 py-2 text-sm font-medium text-gray-600 transition-colors duration-250 hover:bg-surface-100 hover:text-gray-900"
          active-class="!bg-primary-50 !text-primary-700"
        >
          {{ item.label }}
        </RouterLink>
      </nav>
    </div>
  </header>
</template>
