<script setup lang="ts">
defineProps<{
  open: boolean
  title: string
  message: string
  confirmLabel?: string
  cancelLabel?: string
  variant?: 'danger' | 'default'
}>()
const emit = defineEmits<{
  confirm: []
  cancel: []
}>()
</script>

<template>
  <Teleport to="body">
    <Transition
      enter-active-class="transition ease-out duration-200"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition ease-in duration-150"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="open"
        class="fixed inset-0 z-[60] flex items-center justify-center p-4"
        @click.self="emit('cancel')"
      >
        <div class="fixed inset-0 bg-gray-900/50" aria-hidden="true"></div>
        <div
          class="relative w-full max-w-md rounded-2xl bg-white p-6 shadow-xl"
          role="dialog"
          aria-modal="true"
          :aria-labelledby="($attrs['aria-labelledby'] as string | undefined)"
        >
          <div class="flex gap-4">
            <div
              class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full"
              :class="variant === 'danger' ? 'bg-red-100' : 'bg-surface-100'"
            >
              <svg
                v-if="variant === 'danger'"
                class="h-6 w-6 text-red-600"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
              <svg v-else class="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="flex-1 min-w-0">
              <h3 class="text-lg font-semibold text-gray-900">{{ title }}</h3>
              <p class="mt-2 text-sm text-gray-500">{{ message }}</p>
              <div class="mt-6 flex gap-3">
                <button
                  type="button"
                  class="btn-secondary flex-1"
                  @click="emit('cancel')"
                >
                  {{ cancelLabel ?? 'Cancelar' }}
                </button>
                <button
                  type="button"
                  class="flex-1"
                  :class="variant === 'danger' ? 'rounded-lg bg-red-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-red-700' : 'btn-primary'"
                  @click="emit('confirm')"
                >
                  {{ confirmLabel ?? 'Confirmar' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
