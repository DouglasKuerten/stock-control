<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useStockMovementStore } from '@/stores/stockMovement'
import { formatCurrency } from '@/utils/currency'
import { MovementType } from '@/models/enums'
import type { StockMovement } from '@/models/stockMovement'
import type { StockMovementFormValues } from '@/utils/stockMovementSchema'
import PageHeader from '@/components/PageHeader.vue'
import Card from '@/components/Card.vue'
import Badge from '@/components/Badge.vue'
import EmptyState from '@/components/EmptyState.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'
import FormModal from '@/components/FormModal.vue'
import StockMovementForm from '@/components/StockMovementForm.vue'

const store = useStockMovementStore()
const { list, products } = storeToRefs(store)
const deleteTarget = ref<number | null>(null)
const formModalOpen = ref(false)
const formEditingId = ref<number | null>(null)

const formModalTitle = computed(() =>
  formEditingId.value ? 'Editar movimentação' : 'Nova movimentação'
)

const movementInitialValues = computed((): Partial<StockMovementFormValues> | undefined => {
  if (!formEditingId.value) return undefined
  const m = list.value.find((x) => x.id === formEditingId.value) as StockMovement | undefined
  if (!m) return undefined
  let saleDate: string | undefined
  if (m.saleDate) {
    const d = new Date(m.saleDate)
    const pad = (n: number) => String(n).padStart(2, '0')
    saleDate = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
  }
  return {
    productId: m.product?.id,
    movementType: m.movementType,
    quantity: m.quantity,
    saleValue: m.saleValue ?? undefined,
    saleDate,
  }
})

function typeLabel(type: MovementType) {
  return type === MovementType.ENTRY ? 'Entrada' : 'Saída'
}

function typeBadgeVariant(type: MovementType) {
  return type === MovementType.ENTRY ? 'success' : 'warning'
}

function openFormModal(movement?: StockMovement) {
  formEditingId.value = movement?.id ?? null
  formModalOpen.value = true
}

function closeFormModal() {
  formModalOpen.value = false
  formEditingId.value = null
}

async function onMovementSubmit(values: StockMovementFormValues) {
  const dto = {
    productId: values.productId,
    movementType: values.movementType,
    quantity: values.quantity,
    saleValue: values.movementType === 'EXIT' ? values.saleValue : undefined,
    saleDate: values.movementType === 'EXIT' && values.saleDate ? values.saleDate : undefined,
  }
  if (formEditingId.value != null) {
    await store.update(formEditingId.value, dto)
  } else {
    await store.create(dto)
  }
  closeFormModal()
}

function openDeleteConfirm(id: number) {
  deleteTarget.value = id
}

function closeDeleteConfirm() {
  deleteTarget.value = null
}

async function confirmDelete() {
  if (deleteTarget.value == null) return
  await store.remove(deleteTarget.value)
  closeDeleteConfirm()
}

onMounted(() => {
  store.fetchAll()
  store.fetchProducts()
})
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <PageHeader title="Movimentações" subtitle="Histórico de entradas e saídas de estoque" />
      <button type="button" class="btn-primary shrink-0" @click="openFormModal()">
        <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Nova movimentação
      </button>
    </div>
    <Card :padding="false">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Produto</th>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Tipo</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Qtd</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Valor venda</th>
              <th class="px-4 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-gray-500">Data venda</th>
              <th class="px-4 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-gray-500">Ações</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200 bg-white">
            <tr
              v-for="(m, i) in list"
              :key="m.id"
              class="transition-colors hover:bg-gray-50"
              :class="{ 'bg-gray-50/50': i % 2 === 1 }"
            >
              <td class="px-4 py-3.5">
                <span class="text-sm font-medium text-gray-900">{{ m.product?.code }}</span>
                <span class="block text-sm text-gray-500">{{ m.product?.description }}</span>
              </td>
              <td class="whitespace-nowrap px-4 py-3.5">
                <Badge :variant="typeBadgeVariant(m.movementType)">{{ typeLabel(m.movementType) }}</Badge>
              </td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm font-medium text-gray-900">{{ m.quantity }}</td>
              <td class="whitespace-nowrap px-4 py-3.5 text-right text-sm text-gray-900">
                {{ m.saleValue != null ? formatCurrency(m.saleValue) : '—' }}
              </td>
              <td class="whitespace-nowrap px-4 py-3.5 text-sm text-gray-600">
                {{ m.saleDate ? new Date(m.saleDate).toLocaleString('pt-BR') : '—' }}
              </td>
              <td class="whitespace-nowrap px-4 py-3.5">
                <div class="flex items-center justify-end gap-1">
                  <button type="button" class="btn-ghost rounded-lg" @click="openFormModal(m)">
                    Editar
                  </button>
                  <button
                    type="button"
                    class="btn-ghost btn-danger rounded-lg"
                    @click="openDeleteConfirm(m.id)"
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
        title="Nenhuma movimentação"
        description="Registre entradas e saídas para manter o controle do estoque."
        :action="true"
      >
        <button type="button" class="btn-primary mt-4" @click="openFormModal()">
          Nova movimentação
        </button>
      </EmptyState>
    </Card>
    <FormModal
      :open="formModalOpen"
      :title="formModalTitle"
      @close="closeFormModal"
    >
      <StockMovementForm
        v-if="products.length > 0"
        :key="formEditingId ?? 'new'"
        :products="products"
        :initial-values="movementInitialValues"
        @submit="onMovementSubmit"
        @cancel="closeFormModal"
      />
      <div v-else class="flex items-center gap-3 py-4">
        <div class="h-10 w-10 animate-pulse rounded-full bg-gray-200"></div>
        <p class="text-sm text-gray-500">Carregando produtos...</p>
      </div>
    </FormModal>
    <ConfirmModal
      :open="deleteTarget !== null"
      title="Excluir movimentação"
      message="Tem certeza que deseja excluir esta movimentação? Esta ação não pode ser desfeita."
      confirm-label="Excluir"
      variant="danger"
      @confirm="confirmDelete"
      @cancel="closeDeleteConfirm"
    />
  </div>
</template>
