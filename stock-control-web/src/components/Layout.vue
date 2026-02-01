<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppNav from './AppNav.vue'
import AppLoading from './AppLoading.vue'
import AppError from './AppError.vue'
</script>

<template>
  <div class="min-h-screen bg-surface-50">
    <AppNav />
    <AppLoading />
    <AppError />
    <main class="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8">
      <RouterView />
    </main>
  </div>
</template>
