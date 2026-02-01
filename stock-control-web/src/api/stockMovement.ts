import { api } from './client'
import type { StockMovement, StockMovementDTO } from '@/models/stockMovement'

export const stockMovementApi = {
  getAll(): Promise<StockMovement[]> {
    return api.get<StockMovement[]>('/stock-movement').then((r) => r.data)
  },
  getById(id: number): Promise<StockMovement> {
    return api.get<StockMovement>(`/stock-movement/${id}`).then((r) => r.data)
  },
  create(dto: StockMovementDTO): Promise<StockMovement> {
    return api.post<StockMovement>('/stock-movement', dto).then((r) => r.data)
  },
  update(id: number, dto: StockMovementDTO): Promise<StockMovement> {
    return api.put<StockMovement>(`/stock-movement/${id}`, dto).then((r) => r.data)
  },
  delete(id: number): Promise<void> {
    return api.delete(`/stock-movement/${id}`)
  },
}
