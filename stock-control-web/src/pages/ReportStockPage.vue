<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useReportsStore } from '@/stores/reports'
import { ProductType } from '@/models/enums'
import PageHeader from '@/components/PageHeader.vue'
import Card from '@/components/Card.vue'
import Badge from '@/components/Badge.vue'
import EmptyState from '@/components/EmptyState.vue'

const store = useReportsStore()
const { stockByType, filterType } = storeToRefs(store)

const productTypeOptions = [
  { value: '' as const, label: 'Todos' },
  { value: ProductType.ELECTRONIC, label: 'Eletrônico' },
  { value: ProductType.HOME_APPLIANCE, label: 'Eletrodoméstico' },
  { value: ProductType.FURNITURE, label: 'Móvel' },
]

function typeBadgeVariant(type: ProductType) {
  const map: Record<ProductType, 'default' | 'success' | 'info'> = {
    [ProductType.ELECTRONIC]: 'info',
    [ProductType.HOME_APPLIANCE]: 'success',
    [ProductType.FURNITURE]: 'default',
  }
  return map[type] ?? 'default'
}

function onFilterChange(value: ProductType | '') {
  store.setFilterType(value)
  store.fetchStockByType()
}

const totalAvailable = computed(() =>
  stockByType.value.reduce((acc, r) => acc + (r.availableQuantity ?? 0), 0)
)

const totalExits = computed(() =>
  stockByType.value.reduce((acc, r) => acc + (r.totalExitQuantity ?? 0), 0)
)

onMounted(() => {
  store.fetchStockByType()
})
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <PageHeader title="Resumo de estoque" subtitle="Visão consolidada por tipo de produto" />
    </div>
    <div class="flex flex-wrap items-center gap-3">
      <label for="filterType" class="text-sm font-medium text-gray-700">Filtrar por tipo:</label>
      <select
        id="filterType"
        v-model="filterType"
        class="input-base max-w-[200px]"
        @change="onFilterChange(filterType)"
      >
        <option v-for="opt in productTypeOptions" :key="String(opt.value)" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </div>
    <div v-if="stockByType.length > 0" class="grid gap-4 sm:grid-cols-2">
      <Card class="flex items-center gap-4">
        <div class="flex h-12 w-12 items-center justify-center rounded-xl bg-primary-100">
          <svg class="h-6 w-6 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8 4-8-4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
          </svg>
        </div>
        <div>
          <p class="text-sm font-medium text-gray-500">Disponível</p>
          <p class="text-2xl font-semibold text-gray-900">{{ totalAvailable }}</p>
        </div>
      </Card>
      <Card class="flex items-center gap-4">
        <div class="flex h-12 w-12 items-center justify-center rounded-xl bg-amber-100">
          <svg class="h-6 w-6 text-amber-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
        </div>
        <div>
          <p class="text-sm font-medium text-gray-500">Total de saídas</p>
          <p class="text-2xl font-semibold text-gray-900">{{ totalExits }}</p>
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
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Tipo</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Saídas</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Disponível</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-surface-200 bg-white">
            <tr
              v-for="(r, i) in stockByType"
              :key="r.productId"
              class="transition-colors hover:bg-surface-50"
              :class="{ 'bg-surface-50/50': i % 2 === 1 }"
            >
              <td class="whitespace-nowrap px-4 py-3.5 text-sm font-medium text-gray-900">{{ r.code }}</td>
              <td class="px-4 py-3.5 text-sm text-gray-600">{{ r.description }}</td>
              <td class="whitespace-nowrap px-4 py-3.5">
                <Badge :variant="typeBadgeVariant(r.productType)">{{ r.productType }}</Badge>
              </td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm text-gray-900">{{ r.totalExitQuantity }}</td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm font-semibold text-primary-700">{{ r.availableQuantity }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <EmptyState
        v-if="stockByType.length === 0"
        title="Nenhum dado encontrado"
        description="Não há produtos ou o filtro não retornou resultados."
      />
    </Card>
  </div>
</template>
