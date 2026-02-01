import { z } from 'zod'
import { MovementType } from '@/models/enums'

const movementTypeEnum = z.nativeEnum(MovementType)

export const stockMovementFormSchema = z
  .object({
    productId: z.number().positive('Selecione um produto'),
    movementType: movementTypeEnum,
    quantity: z.number().int().min(1, 'Quantidade deve ser pelo menos 1'),
    saleValue: z.number().optional().nullable(),
    saleDate: z.string().optional().nullable(),
  })
  .superRefine((data, ctx) => {
    if (data.movementType === MovementType.EXIT) {
      if (data.saleValue == null || data.saleValue <= 0) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'Valor de venda é obrigatório para saída',
          path: ['saleValue'],
        })
      }
      if (!data.saleDate || data.saleDate.trim() === '') {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'Data de venda é obrigatória para saída',
          path: ['saleDate'],
        })
      }
    }
  })

export type StockMovementFormValues = z.infer<typeof stockMovementFormSchema>
