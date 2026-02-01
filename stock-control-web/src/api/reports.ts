import { api } from './client'
import type { ProductStockSummaryDTO, ProductProfitDTO } from '@/models/reports'
import type { ProductType } from '@/models/enums'

export const reportsApi = {
  productsByType(productType?: ProductType): Promise<ProductStockSummaryDTO[]> {
    const params = productType != null ? { productType } : {}
    return api.get<ProductStockSummaryDTO[]>('/reports/products', { params }).then((r) => r.data)
  },
  profitByProduct(): Promise<ProductProfitDTO[]> {
    return api.get<ProductProfitDTO[]>('/reports/profit').then((r) => r.data)
  },
}
