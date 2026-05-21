<script setup lang="ts">
defineProps<{
  title: string
  description: string
}>()
</script>

<template>
  <section class="page-container">
    <div class="page-header">
      <h2>{{ title }}</h2>
      <p>{{ description }}</p>
    </div>
    <div class="page-body">
      <slot />
    </div>
  </section>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: #1f2329;
}

.page-header p {
  margin: 8px 0 0;
  color: #667085;
  line-height: 1.6;
}
</style>
