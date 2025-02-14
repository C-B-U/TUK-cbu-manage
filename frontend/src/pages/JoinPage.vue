<template>
  <v-container class="join-page">
    <v-row align-items="center" justify="center" class="join-row">
      <v-col cols="12" sm="8" md="6" lg="6">
        <div class="join-wrapper">
          <!-- 단계 표시: 회원가입 제목 바로 위에 위치 -->
          <div class="step-indicator">
            <div :class="['step', { active: currentStep === 1 }]"></div>
            <div :class="['step', { active: currentStep === 2 }]"></div>
          </div>

          <h2 class="join-title">회원가입</h2>
          <v-card-text>
            <!-- currentStep에 따라 StepOne 혹은 StepTwo 컴포넌트를 렌더링 -->
            <component :is="currentComponent" @verified="handleVerified" />
          </v-card-text>
          <h4>
            지원 시 닉네임 기억이 나지 않는다면? &nbsp;
            <a href="https://www.instagram.com/tukorea_cbu/#" class="custom-link" target="_blank"
              rel="noopener noreferrer">
              합격자 발표 확인해보기 (클릭!)
            </a>
          </h4>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import StepOne from '../components/signup/stepOne.vue';
import StepTwo from '../components/signup/stepTwo.vue';

// 현재 단계 상태 (초기값은 1단계)
const currentStep = ref(1);
// StepOne에서 합격자 인증 후 반환된 이름을 저장
const verifiedData = ref(null);

// 현재 단계에 따른 컴포넌트 선택
const currentComponent = computed(() => {
  return currentStep.value === 1 ? StepOne : StepTwo;
});

const handleVerified = (data: any) => {
  verifiedData.value = data;
  currentStep.value = 2;
};

</script>

<style scoped>
.join-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 16px;
  box-sizing: border-box;
}

.join-row {
  width: 130%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.join-wrapper {
  position: relative;
  padding-top: 40px;
}

.join-title {
  font-size: 1.7rem;
  font-weight: bold;
  margin-bottom: 10px;
  padding: 16px;
  color: #333;
  text-align: center;
}

.step-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.step {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: #ccc;
  margin: 0 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  color: white;
}

.step.active {
  background-color: var(--mainColor);
}

/* 버튼 스타일 */
.custom-btn {
  background-color: var(--mainColor);
  height: 56px;
  color: #fff;
  border-radius: 5px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  font-size: 1rem;
  text-transform: uppercase;
  transition: transform 0.2s ease;
}

.v-btn {
  letter-spacing: normal !important;
}

.email-btn-col {
  display: flex;
  align-items: flex-start;
}

.custom-link {
  color: black;
  font-weight: bold;
  text-decoration: none;
}

.custom-link:hover {
  text-decoration: none;
}
</style>