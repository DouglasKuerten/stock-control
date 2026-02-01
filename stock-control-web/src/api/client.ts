import axios from 'axios'
import { useAppStore } from '@/stores/app'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

export const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const msg = (() => {
      if (axios.isAxiosError(error)) {
        const data = error.response?.data
        if (typeof data === 'string') return data
        if (data && typeof data === 'object' && 'message' in data) {
          return String((data as { message?: unknown }).message)
        }
      }
      return error instanceof Error ? error.message : 'Erro de conexão'
    })()
    try {
      useAppStore().setError(msg)
    } catch {
      console.error(msg)
    }
    return Promise.reject(new Error(msg))
  }
)
