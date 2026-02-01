<script setup lang="ts">
import { watch } from 'vue'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import { productFormSchema, type ProductFormValues } from '@/utils/productSchema'
import { ProductType } from '@/models/enums'
const props = withDefaults(
  defineProps<{ initialValues?: Partial<ProductFormValues> }>(),
  { initialValues: undefined }
)
const emit = defineEmits<{
  submit: [values: ProductFormValues]
  cancel: []
}>()

const { handleSubmit, defineField, errors, setValues } = useForm({
  validationSchema: toTypedSchema(productFormSchema),
  initialValues: props.initialValues,
})

watch(
  () => props.initialValues,
  (v) => {
    if (v) setValues(v)
  },
  { immediate: true }
)

const [code, codeAttrs] = defineField('code')
const [description, descriptionAttrs] = defineField('description')
const [productType, productTypeAttrs] = defineField('productType')
const [supplierValue, supplierValueAttrs] = defineField('supplierValue')
const [stockQuantity, stockQuantityAttrs] = defineField('stockQuantity')

const onSubmit = handleSubmit((values) => {
  emit('submit', {
    ...values,
    supplierValue: Number(values.supplierValue),
    stockQuantity: Number(values.stockQuantity),
  })
})

const productTypes = [
  { value: ProductType.ELECTRONIC, label: 'Eletrônico' },
  { value: ProductType.HOME_APPLIANCE, label: 'Eletrodoméstico' },
  { value: ProductType.FURNITURE, label: 'Móvel' },
]
</script>

<template>
  <form class="space-y-5" @submit="onSubmit">
      <div class="grid gap-5 sm:grid-cols-2">
        <div>
          <label for="code" class="mb-2 block text-sm font-medium text-gray-700">Código</label>
          <input
            id="code"
            v-model="code"
            v-bind="codeAttrs"
            type="text"
            placeholder="Ex: PRD-001"
            class="input-base"
            :class="{ 'input-error': errors.code }"
          />
          <p v-if="errors.code" class="mt-1.5 text-sm text-red-600">{{ errors.code }}</p>
        </div>
        <div>
          <label for="productType" class="mb-2 block text-sm font-medium text-gray-700">Tipo</label>
          <select
            id="productType"
            v-model="productType"
            v-bind="productTypeAttrs"
            class="input-base"
            :class="{ 'input-error': errors.productType }"
          >
            <option value="">Selecione</option>
            <option v-for="opt in productTypes" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
          </select>
          <p v-if="errors.productType" class="mt-1.5 text-sm text-red-600">{{ errors.productType }}</p>
        </div>
      </div>
      <div>
        <label for="description" class="mb-2 block text-sm font-medium text-gray-700">Descrição</label>
        <input
          id="description"
          v-model="description"
          v-bind="descriptionAttrs"
          type="text"
          placeholder="Descrição do produto"
          class="input-base"
          :class="{ 'input-error': errors.description }"
        />
        <p v-if="errors.description" class="mt-1.5 text-sm text-red-600">{{ errors.description }}</p>
      </div>
      <div class="grid gap-5 sm:grid-cols-2">
        <div>
          <label for="supplierValue" class="mb-2 block text-sm font-medium text-gray-700">Valor do fornecedor (R$)</label>
          <input
            id="supplierValue"
            v-model.number="supplierValue"
            v-bind="supplierValueAttrs"
            type="number"
            step="0.01"
            min="0"
            placeholder="0,00"
            class="input-base"
            :class="{ 'input-error': errors.supplierValue }"
          />
          <p v-if="errors.supplierValue" class="mt-1.5 text-sm text-red-600">{{ errors.supplierValue }}</p>
        </div>
        <div>
          <label for="stockQuantity" class="mb-2 block text-sm font-medium text-gray-700">Quantidade em estoque</label>
          <input
            id="stockQuantity"
            v-model.number="stockQuantity"
            v-bind="stockQuantityAttrs"
            type="number"
            min="0"
            placeholder="0"
            class="input-base"
            :class="{ 'input-error': errors.stockQuantity }"
          />
          <p v-if="errors.stockQuantity" class="mt-1.5 text-sm text-red-600">{{ errors.stockQuantity }}</p>
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
