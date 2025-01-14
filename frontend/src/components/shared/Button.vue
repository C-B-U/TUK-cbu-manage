<template>
  <!-- 버튼 속성과 클래스 동적 제어 -->
  <button :class="['custom-button', buttonClass]" :type="type">
    {{ buttonText }}
  </button>
</template>

<script lang="ts" setup>
import { ButtonType, ButtonStatus } from '@/types';
import { computed } from 'vue';

// Props 정의 및 기본값 설정
const props = withDefaults(defineProps<{
  type?: ButtonType;
  status?: ButtonStatus;
}>(), {
  type: 'button',
  status: 'default',
});

// 클래스와 텍스트 동적 계산
const buttonClass = computed(() =>
  ({
    active: 'custom-button--active',
    disabled: 'custom-button--disabled',
    default: '',
  }[props.status] || '')
);

const buttonText = computed(() =>
  ({
    active: '동아리 지원하기',
    disabled: '신청기간이 아닙니다',
    default: 'Button',
  }[props.status] || 'Button')
);
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
}

.custom-button--disabled {
  border: 1px solid #CCCCCC;
  background-color: #CCCCCC;
  color: var(--semiDarkText);
}
</style>
