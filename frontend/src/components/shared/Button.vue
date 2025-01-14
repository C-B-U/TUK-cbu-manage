<template>
  <!-- 상위 컴포넌트에서 active, disabled로 스타일링 제어 -->
  <button :class="['custom-button', buttonClass]" :type="type">
    {{ buttonText }}
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
    status: {
      type: String,
      default: 'default',
    },
  },
  computed: {
    buttonClass() {
      switch (this.status) {
        case 'active':
          return 'custom-button--active';
        case 'disabled':
          return 'custom-button--disabled';
        default:
          return '';
      }
    },

    buttonText() {
      switch (this.status) {
        case 'active':
          return '동아리 지원하기';
        case 'disabled':
          return '신청기간이 아닙니다';
        default:
          return 'Error';
      }
    },
  },
};
</script>

<style scoped>
.custom-button {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 600;

  border-radius: 100px;

  cursor: pointer;
  transition: all 0.3s;
}

.custom-button--active {
  border: 1px solid var(--mainColor);
  background-color: var(--mainColor);
  color: white;
}

.custom-button--active:hover {
  border: 1px solid var(--mainHover);
  background-color: var(--mainHover);
  color: white;
}

.custom-button--disabled {
  border: 1px solid #CCCCCC;
  background-color: #CCCCCC;
  color: var(--semiDarkText);
}
</style>
