<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useReportsStore } from '@/stores/reports'
import { formatCurrency } from '@/utils/currency'
import PageHeader from '@/components/PageHeader.vue'
import Card from '@/components/Card.vue'
import EmptyState from '@/components/EmptyState.vue'

const store = useReportsStore()
const { profitByProduct } = storeToRefs(store)

const totalProfit = computed(() =>
  profitByProduct.value.reduce((acc, r) => acc + (r.totalProfit ?? 0), 0)
)
const totalSalesValue = computed(() =>
  profitByProduct.value.reduce((acc, r) => acc + (r.totalSalesValue ?? 0), 0)
)
const totalExitQuantity = computed(() =>
  profitByProduct.value.reduce((acc, r) => acc + (r.totalExitQuantity ?? 0), 0)
)

onMounted(() => {
  store.fetchProfitByProduct()
})
</script>

<template>
  <div class="space-y-6">
    <PageHeader title="Lucro por produto" subtitle="Análise de rentabilidade das vendas" />
    <div v-if="profitByProduct.length > 0" class="grid gap-4 sm:grid-cols-3">
      <Card class="flex items-center gap-4">
        <div class="flex h-12 w-12 items-center justify-center rounded-xl bg-indigo-100">
          <svg class="h-6 w-6 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div>
          <p class="text-sm font-medium text-gray-500">Total de vendas</p>
          <p class="text-2xl font-semibold text-indigo-700">{{ formatCurrency(totalSalesValue) }}</p>
        </div>
      </Card>
      <Card class="flex items-center gap-4">
        <div class="flex h-12 w-12 items-center justify-center rounded-xl bg-emerald-100">
          <svg class="h-6 w-6 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div>
          <p class="text-sm font-medium text-gray-500">Lucro total</p>
          <p class="text-2xl font-semibold text-emerald-700">{{ formatCurrency(totalProfit) }}</p>
        </div>
      </Card>
      <Card class="flex items-center gap-4">
        <div class="flex h-12 w-12 items-center justify-center rounded-xl bg-sky-100">
          <svg class="h-6 w-6 text-sky-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4m6 6L4 12l6-6" />
          </svg>
        </div>
        <div>
          <p class="text-sm font-medium text-gray-500">Total de saídas</p>
          <p class="text-2xl font-semibold text-sky-700">{{ totalExitQuantity }}</p>
        </div>
      </Card>
    </div>
    <Card :padding="false">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-surface-200">
          <thead class="bg-surface-50">
            <tr>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Código</th>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Descrição</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Qtd. saídas</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Total vendas</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Lucro</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-surface-200 bg-white">
            <tr
              v-for="(r, i) in profitByProduct"
              :key="r.productId"
              class="transition-colors hover:bg-surface-50"
              :class="{ 'bg-surface-50/50': i % 2 === 1 }"
            >
              <td class="whitespace-nowrap px-4 py-3.5 text-sm font-medium text-gray-900">{{ r.code }}</td>
              <td class="px-4 py-3.5 text-sm text-gray-600">{{ r.description }}</td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm text-gray-900">{{ r.totalExitQuantity }}</td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm text-gray-900">
                {{ formatCurrency(r.totalSalesValue) }}
              </td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm font-semibold text-emerald-700">
                {{ formatCurrency(r.totalProfit) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <EmptyState
        v-if="profitByProduct.length === 0"
        title="Nenhum dado encontrado"
        description="Não há vendas registradas para calcular o lucro."
      />
    </Card>
  </div>
</template>
