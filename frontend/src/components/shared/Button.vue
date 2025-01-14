<template>
  <button :class="['custom-button']" :type="type">
    <slot>Button</slot>
  </button>
</template>

<script>
export default {
  name: 'Button',
  props: {
    type: {
      type: String,
      default: 'button',
    },
  }
};
</script>

<style scoped>
.custom-button {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 600;

  border:1px solid #CCCCCC;
  border-radius: 100px;
  
  cursor: pointer;
  background-color: #CCCCCC;
  color: var(--semiDarkText);
  transition: all 0.3s;
}
</style>
