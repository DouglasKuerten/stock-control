import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productsApi } from '@/api/products'
import { useAppStore } from './app'
import type { Product, ProductDTO } from '@/models/product'
import type { ProductType } from '@/models/enums'

export const useProductsStore = defineStore('products', () => {
  const app = useAppStore()
  const list = ref<Product[]>([])
  const current = ref<Product | null>(null)
  const filterType = ref<ProductType | ''>('')

  async function fetchAll() {
    app.setLoading(true)
    app.clearError()
    try {
      const type = filterType.value || undefined
      list.value = await productsApi.getAll(type)
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar produtos')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function fetchById(id: number) {
    app.setLoading(true)
    app.clearError()
    try {
      current.value = await productsApi.getById(id)
      return current.value
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar produto')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function create(dto: ProductDTO) {
    app.setLoading(true)
    app.clearError()
    try {
      const created = await productsApi.create(dto)
      list.value = [...list.value, created]
      return created
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao criar produto')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function update(id: number, dto: ProductDTO) {
    app.setLoading(true)
    app.clearError()
    try {
      const updated = await productsApi.update(id, dto)
      const idx = list.value.findIndex((p) => p.id === id)
      if (idx >= 0) {
        const next = [...list.value]
        next[idx] = updated
        list.value = next
      }
      if (current.value?.id === id) current.value = updated
      return updated
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao atualizar produto')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function remove(id: number) {
    app.setLoading(true)
    app.clearError()
    try {
      await productsApi.delete(id)
      list.value = list.value.filter((p) => p.id !== id)
      if (current.value?.id === id) current.value = null
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao excluir produto')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  function setFilterType(type: ProductType | '') {
    filterType.value = type
  }

  return {
    list,
    current,
    filterType,
    fetchAll,
    fetchById,
    create,
    update,
    remove,
    setFilterType,
  }
})
