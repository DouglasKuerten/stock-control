import type { MovementType } from './enums'
import type { Product } from './product'

export interface StockMovementDTO {
  productId: number
  movementType: MovementType
  quantity: number
  saleValue?: number | null
  saleDate?: string | null
}

export interface StockMovement {
  id: number
  product: Product
  movementType: MovementType
  quantity: number
  saleValue?: number | null
  saleDate?: string | null
}
