<template>
  <button 
    class="custom-button"
    :class="buttonClass"
    @click="handleClick"
    :disabled="!userStore.isAdmin"
  >
    {{ buttonText }}
  </button>
</template>

<script setup>
import { computed } from "vue";
import { useUserStore } from "../../stores/userStore";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const router = useRouter();

// ✅ 버튼 스타일 동적 클래스
const buttonClass = computed(() =>
  userStore.isAdmin ? "custom-button--active" : "custom-button--disabled"
);

// ✅ 버튼 텍스트 동적 설정
const buttonText = computed(() =>
  userStore.isAdmin ? "멤버 관리로 이동" : "신청기간이 아닙니다"
);

// ✅ 클릭 이벤트 (관리자만 이동 가능)
const handleClick = () => {
  if (userStore.isAdmin) {
    router.push("/memberManage");
  }
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

/* ✅ 관리자 버튼 */
.custom-button--active {
  border: 1px solid var(--mainColor);
  background-color: var(--mainColor);
  color: white;
}

.custom-button--active:hover {
  border: 1px solid var(--mainHover);
  background-color: var(--mainHover);
}

/* ✅ 일반 사용자 버튼 */
.custom-button--disabled {
  border: 1px solid #CCCCCC;
  background-color: #CCCCCC;
  color: var(--semiDarkText);
  cursor: not-allowed;
}
</style>
