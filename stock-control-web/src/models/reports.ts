import type { ProductType } from './enums'

export interface ProductStockSummaryDTO {
  productId: number
  code: string
  description: string
  productType: ProductType
  totalExitQuantity: number
  availableQuantity: number
}

export interface ProductProfitDTO {
  productId: number
  code: string
  description: string
  totalExitQuantity: number
  totalSalesValue: number
  totalProfit: number
}
