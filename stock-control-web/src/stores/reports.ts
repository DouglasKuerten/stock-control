import { defineStore } from 'pinia'
import { ref } from 'vue'
import { reportsApi } from '@/api/reports'
import { useAppStore } from './app'
import type { ProductStockSummaryDTO, ProductProfitDTO } from '@/models/reports'
import type { ProductType } from '@/models/enums'

export const useReportsStore = defineStore('reports', () => {
  const app = useAppStore()
  const stockByType = ref<ProductStockSummaryDTO[]>([])
  const profitByProduct = ref<ProductProfitDTO[]>([])
  const filterType = ref<ProductType | ''>('')

  async function fetchStockByType() {
    app.setLoading(true)
    app.clearError()
    try {
      const type = filterType.value || undefined
      stockByType.value = await reportsApi.productsByType(type)
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar resumo de estoque')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  async function fetchProfitByProduct() {
    app.setLoading(true)
    app.clearError()
    try {
      profitByProduct.value = await reportsApi.profitByProduct()
    } catch (e: unknown) {
      app.setError(e instanceof Error ? e.message : 'Erro ao carregar lucro por produto')
      throw e
    } finally {
      app.setLoading(false)
    }
  }

  function setFilterType(type: ProductType | '') {
    filterType.value = type
  }

  return {
    stockByType,
    profitByProduct,
    filterType,
    fetchStockByType,
    fetchProfitByProduct,
    setFilterType,
  }
})
