import { api } from './client'
import type { Product, ProductDTO } from '@/models/product'
import type { ProductType } from '@/models/enums'

export const productsApi = {
  getAll(productType?: ProductType): Promise<Product[]> {
    const params = productType != null ? { productType } : {}
    return api.get<Product[]>('/products', { params }).then((r) => r.data)
  },
  getById(id: number): Promise<Product> {
    return api.get<Product>(`/products/${id}`).then((r) => r.data)
  },
  create(dto: ProductDTO): Promise<Product> {
    return api.post<Product>('/products', dto).then((r) => r.data)
  },
  update(id: number, dto: ProductDTO): Promise<Product> {
    return api.put<Product>(`/products/${id}`, dto).then((r) => r.data)
  },
  delete(id: number): Promise<void> {
    return api.delete(`/products/${id}`)
  },
}
