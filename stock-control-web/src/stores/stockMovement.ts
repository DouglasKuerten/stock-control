import { defineStore } from 'pinia'
import { ref } from 'vue'
import { stockMovementApi } from '@/api/stockMovement'
import { productsApi } from '@/api/products'
import { useAppStore } from './app'
import type { StockMovement, StockMovementDTO } from '@/models/stockMovement'
import type { Product } from '@/models/product'

export const useStockMovementStore = defineStore('stockMovement', () => {
  const app = useAppStore()
  const list = ref<StockMovement[]>([])
  const current = ref<StockMovement | null>(null)
  const products = ref<Product[]>([])

  async function fetchAll() {
    app.setLoading(true)
    app.clearError()
    try {
      list.value = await stockMovementApi.getAll()
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar movimentações')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function fetchProducts() {
    try {
      products.value = await productsApi.getAll()
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar produtos')
      throw e
    }
  }

  async function fetchById(id: number) {
    app.setLoading(true)
    app.clearError()
    try {
      current.value = await stockMovementApi.getById(id)
      return current.value
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar movimentação')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function create(dto: StockMovementDTO) {
    app.setLoading(true)
    app.clearError()
    try {
      const created = await stockMovementApi.create(dto)
      list.value = [created, ...list.value]
      return created
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao criar movimentação')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function update(id: number, dto: StockMovementDTO) {
    app.setLoading(true)
    app.clearError()
    try {
      const updated = await stockMovementApi.update(id, dto)
      const idx = list.value.findIndex((m) => m.id === id)
      if (idx >= 0) list.value[idx] = updated
      if (current.value?.id === id) current.value = updated
      return updated
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao atualizar movimentação')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function remove(id: number) {
    app.setLoading(true)
    app.clearError()
    try {
      await stockMovementApi.delete(id)
      list.value = list.value.filter((m) => m.id !== id)
      if (current.value?.id === id) current.value = null
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao excluir movimentação')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  return {
    list,
    current,
    products,
    fetchAll,
    fetchProducts,
    fetchById,
    create,
    update,
    remove,
  }
})
