import { z } from 'zod'
import { ProductType } from '@/models/enums'

const productTypeEnum = z.nativeEnum(ProductType)

export const productFormSchema = z.object({
  code: z.string().min(1, 'Código é obrigatório'),
  description: z.string().min(1, 'Descrição é obrigatória'),
  productType: productTypeEnum,
  supplierValue: z.number().positive('Valor deve ser positivo'),
  stockQuantity: z.number().int().min(0, 'Quantidade não pode ser negativa'),
})

export type ProductFormValues = z.infer<typeof productFormSchema>
