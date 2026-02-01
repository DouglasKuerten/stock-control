<script setup lang="ts">
defineProps<{
  variant?: 'default' | 'success' | 'warning' | 'info'
}>()
</script>

<template>
  <span
    class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
    :class="{
      'bg-primary-100 text-primary-800': variant === 'default' || !variant,
      'bg-emerald-100 text-emerald-800': variant === 'success',
      'bg-amber-100 text-amber-800': variant === 'warning',
      'bg-blue-100 text-blue-800': variant === 'info',
    }"
  >
    <slot />
  </span>
</template>
