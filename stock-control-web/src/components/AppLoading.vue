<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useAppStore } from '@/stores/app'

const { loading } = storeToRefs(useAppStore())
</script>

<template>
  <Transition
    enter-active-class="transition ease-out duration-150"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition ease-in duration-100"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="loading"
      class="fixed inset-0 z-50 flex items-center justify-center bg-gray-900/20 backdrop-blur-[1px]"
      role="status"
      aria-label="Carregando"
    >
      <div class="flex flex-col items-center gap-3 rounded-2xl bg-white px-8 py-6 shadow-xl">
        <div class="h-10 w-10 animate-spin rounded-full border-2 border-primary-200 border-t-primary-600"></div>
        <p class="text-sm font-medium text-gray-600">Carregando...</p>
      </div>
    </div>
  </Transition>
</template>
