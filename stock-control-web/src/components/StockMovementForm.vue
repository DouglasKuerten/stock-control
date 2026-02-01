<script setup lang="ts">
import { computed, watch } from 'vue'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import { stockMovementFormSchema, type StockMovementFormValues } from '@/utils/stockMovementSchema'
import { MovementType } from '@/models/enums'
import type { Product } from '@/models/product'

const props = withDefaults(
  defineProps<{
    products: Product[]
    initialValues?: Partial<StockMovementFormValues>
  }>(),
  { initialValues: undefined }
)
const emit = defineEmits<{
  submit: [values: StockMovementFormValues]
  cancel: []
}>()

const { handleSubmit, defineField, errors, setValues, values } = useForm({
  validationSchema: toTypedSchema(stockMovementFormSchema),
  initialValues: props.initialValues,
})

watch(
  () => props.initialValues,
  (v) => {
    if (v) setValues(v)
  },
  { immediate: true }
)

const isExit = computed(() => values.movementType === MovementType.EXIT)

const [productId, productIdAttrs] = defineField('productId')
const [movementType, movementTypeAttrs] = defineField('movementType')
const [quantity, quantityAttrs] = defineField('quantity')
const [saleValue, saleValueAttrs] = defineField('saleValue')
const [saleDate, saleDateAttrs] = defineField('saleDate')

const onSubmit = handleSubmit((vals) => {
  const payload: StockMovementFormValues = {
    productId: Number(vals.productId),
    movementType: vals.movementType,
    quantity: Number(vals.quantity),
  }
  if (vals.movementType === MovementType.EXIT) {
    payload.saleValue = vals.saleValue != null ? Number(vals.saleValue) : undefined
    payload.saleDate = vals.saleDate ?? undefined
  }
  emit('submit', payload)
})

const movementTypes = [
  { value: MovementType.ENTRY, label: 'Entrada' },
  { value: MovementType.EXIT, label: 'Saída' },
]
</script>

<template>
  <form class="space-y-5" @submit="onSubmit">
      <div class="grid gap-5 sm:grid-cols-2">
        <div>
          <label for="productId" class="mb-2 block text-sm font-medium text-gray-700">Produto</label>
          <select
            id="productId"
            v-model="productId"
            v-bind="productIdAttrs"
            class="input-base"
            :class="{ 'input-error': errors.productId }"
          >
            <option value="">Selecione um produto</option>
            <option v-for="p in products" :key="p.id" :value="p.id">
              {{ p.code }} — {{ p.description }}
            </option>
          </select>
          <p v-if="errors.productId" class="mt-1.5 text-sm text-red-600">{{ errors.productId }}</p>
        </div>
        <div>
          <label for="movementType" class="mb-2 block text-sm font-medium text-gray-700">Tipo</label>
          <select
            id="movementType"
            v-model="movementType"
            v-bind="movementTypeAttrs"
            class="input-base"
            :class="{ 'input-error': errors.movementType }"
          >
            <option value="">Selecione</option>
            <option v-for="opt in movementTypes" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
          </select>
          <p v-if="errors.movementType" class="mt-1.5 text-sm text-red-600">{{ errors.movementType }}</p>
        </div>
      </div>
      <div>
        <label for="quantity" class="mb-2 block text-sm font-medium text-gray-700">Quantidade</label>
        <input
          id="quantity"
          v-model.number="quantity"
          v-bind="quantityAttrs"
          type="number"
          min="1"
          placeholder="1"
          class="input-base max-w-[200px]"
          :class="{ 'input-error': errors.quantity }"
        />
        <p v-if="errors.quantity" class="mt-1.5 text-sm text-red-600">{{ errors.quantity }}</p>
      </div>
      <div
        v-if="isExit"
        class="space-y-5 rounded-lg border border-amber-200 bg-amber-50/50 p-4"
      >
        <p class="text-sm font-medium text-amber-800">Campos obrigatórios para saída</p>
        <div class="grid gap-5 sm:grid-cols-2">
          <div>
            <label for="saleValue" class="mb-2 block text-sm font-medium text-gray-700">Valor de venda (R$)</label>
            <input
              id="saleValue"
              v-model.number="saleValue"
              v-bind="saleValueAttrs"
              type="number"
              step="0.01"
              min="0"
              placeholder="0,00"
              class="input-base"
              :class="{ 'input-error': errors.saleValue }"
            />
            <p v-if="errors.saleValue" class="mt-1.5 text-sm text-red-600">{{ errors.saleValue }}</p>
          </div>
          <div>
            <label for="saleDate" class="mb-2 block text-sm font-medium text-gray-700">Data de venda</label>
            <input
              id="saleDate"
              v-model="saleDate"
              v-bind="saleDateAttrs"
              type="datetime-local"
              class="input-base"
              :class="{ 'input-error': errors.saleDate }"
            />
            <p v-if="errors.saleDate" class="mt-1.5 text-sm text-red-600">{{ errors.saleDate }}</p>
          </div>
        </div>
      </div>
      <div class="flex flex-wrap gap-3 pt-2">
        <button type="submit" class="btn-primary">
          Salvar
        </button>
        <button type="button" class="btn-secondary" @click="$emit('cancel')">
          Cancelar
        </button>
      </div>
    </form>
</template>
