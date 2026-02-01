<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useProductsStore } from '@/stores/products'
import { ProductType } from '@/models/enums'
import { formatCurrency } from '@/utils/currency'
import type { Product } from '@/models/product'
import type { ProductFormValues } from '@/utils/productSchema'
import PageHeader from '@/components/PageHeader.vue'
import Card from '@/components/Card.vue'
import Badge from '@/components/Badge.vue'
import EmptyState from '@/components/EmptyState.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'
import FormModal from '@/components/FormModal.vue'
import ProductForm from '@/components/ProductForm.vue'

const store = useProductsStore()
const { list, filterType } = storeToRefs(store)
const deleteTarget = ref<{ id: number; code: string } | null>(null)
const formModalOpen = ref(false)
const formEditingId = ref<number | null>(null)

const deleteConfirmMessage = computed(() =>
  deleteTarget.value
    ? `Tem certeza que deseja excluir "${deleteTarget.value.code}"? Esta ação não pode ser desfeita.`
    : ''
)

const formModalTitle = computed(() =>
  formEditingId.value ? 'Editar produto' : 'Novo produto'
)

const productInitialValues = computed((): Partial<ProductFormValues> | undefined => {
  if (!formEditingId.value) return undefined
  const p = list.value.find((x) => x.id === formEditingId.value)
  if (!p) return undefined
  return {
    code: p.code,
    description: p.description,
    productType: p.productType,
    supplierValue: p.supplierValue,
    stockQuantity: p.stockQuantity,
  }
})

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
  store.fetchAll()
}

function openFormModal(product?: Product) {
  formEditingId.value = product?.id ?? null
  formModalOpen.value = true
}

function closeFormModal() {
  formModalOpen.value = false
  formEditingId.value = null
}

async function onProductSubmit(values: ProductFormValues) {
  if (formEditingId.value != null) {
    await store.update(formEditingId.value, values)
  } else {
    await store.create(values)
  }
  closeFormModal()
}

function openDeleteConfirm(p: { id: number; code: string }) {
  deleteTarget.value = p
}

function closeDeleteConfirm() {
  deleteTarget.value = null
}

async function confirmDelete() {
  if (!deleteTarget.value) return
  await store.remove(deleteTarget.value.id)
  closeDeleteConfirm()
}

onMounted(() => {
  store.fetchAll()
})
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <PageHeader title="Produtos" subtitle="Gerencie seu catálogo de produtos" />
      <button type="button" class="btn-primary shrink-0" @click="openFormModal()">
        <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Novo produto
      </button>
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
    <Card :padding="false">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Código</th>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Descrição</th>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Tipo</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Valor</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Estoque</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Ações</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200 bg-white">
            <tr
              v-for="(p, i) in list"
              :key="p.id"
              class="transition-colors hover:bg-gray-50"
              :class="{ 'bg-gray-50/50': i % 2 === 1 }"
            >
              <td class="whitespace-nowrap px-4 py-3.5 text-sm font-medium text-gray-900">{{ p.code }}</td>
              <td class="px-4 py-3.5 text-sm text-gray-600">{{ p.description }}</td>
              <td class="whitespace-nowrap px-4 py-3.5">
                <Badge :variant="typeBadgeVariant(p.productType)">{{ p.productType }}</Badge>
              </td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm font-medium text-gray-900">{{ formatCurrency(p.supplierValue) }}</td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm text-gray-900">{{ p.stockQuantity }}</td>
              <td class="whitespace-nowrap px-4 py-3.5">
                <div class="flex items-center justify-end gap-1">
                  <button type="button" class="btn-ghost rounded-lg" @click="openFormModal(p)">
                    Editar
                  </button>
                  <button
                    type="button"
                    class="btn-ghost btn-danger rounded-lg"
                    @click="openDeleteConfirm({ id: p.id, code: p.code })"
                  >
                    Excluir
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <EmptyState
        v-if="list.length === 0"
        title="Nenhum produto encontrado"
        description="Comece adicionando seu primeiro produto ao catálogo."
        :action="true"
      >
        <button type="button" class="btn-primary mt-4" @click="openFormModal()">
          Novo produto
        </button>
      </EmptyState>
    </Card>
    <FormModal
      :open="formModalOpen"
      :title="formModalTitle"
      @close="closeFormModal"
    >
      <ProductForm
        :key="formEditingId ?? 'new'"
        :initial-values="productInitialValues"
        @submit="onProductSubmit"
        @cancel="closeFormModal"
      />
    </FormModal>
    <ConfirmModal
      :open="!!deleteTarget"
      title="Excluir produto"
      :message="deleteConfirmMessage"
      confirm-label="Excluir"
      variant="danger"
      @confirm="confirmDelete"
      @cancel="closeDeleteConfirm"
    />
  </div>
</template>
