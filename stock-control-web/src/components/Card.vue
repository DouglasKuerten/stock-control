<script setup lang="ts">
const { padding = true, hover = false } = defineProps<{
  padding?: boolean
  hover?: boolean
}>()
</script>

<template>
  <div
    class="overflow-hidden rounded-xl border border-surface-200 bg-white shadow-card transition-shadow duration-250"
    :class="[
      padding ? 'p-6' : 'p-0',
      hover ? 'hover:shadow-card-hover' : '',
    ]"
  >
    <slot />
  </div>
</template>
