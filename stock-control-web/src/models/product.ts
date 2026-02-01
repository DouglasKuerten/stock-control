import type { ProductType } from './enums'

export interface ProductDTO {
  code: string
  description: string
  productType: ProductType
  supplierValue: number
  stockQuantity: number
}

export interface Product extends ProductDTO {
  id: number
}
