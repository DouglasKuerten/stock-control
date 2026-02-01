<script setup lang="ts">
defineProps<{
  title: string
  description?: string
  action?: boolean
}>()
</script>

<template>
  <div class="flex flex-col items-center justify-center py-16 text-center">
    <div class="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-surface-100">
      <svg class="h-8 w-8 text-surface-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
      </svg>
    </div>
    <h3 class="text-base font-medium text-gray-900">{{ title }}</h3>
    <p v-if="description" class="mt-1 max-w-sm text-sm text-gray-500">{{ description }}</p>
    <slot v-if="action" name="action" />
  </div>
</template>
