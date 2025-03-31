<template>
  <v-container class="join-page">
    <v-row align-items="center" justify="center" class="join-row">
      <v-col class="join-col">
        <div class="join-wrapper">
          <div class="step-indicator">
            <div :class="['step', { active: currentStep === 1 }]"></div>
            <div :class="['step', { active: currentStep === 2 }]"></div>
          </div>

          <h2 class="join-title">회원가입</h2>
          <v-card-text class="component-container">
            <component :is="currentComponent" @completed="showModal = true" @verified="handleVerified" />
          </v-card-text>
          <div class="guide-text">
            지원 시 닉네임 기억이 나지 않는다면? &nbsp;
            <a href="https://www.instagram.com/tukorea_cbu/#" class="custom-link" target="_blank" rel="noopener noreferrer">
              합격자 발표 확인해보기 (클릭!)
            </a>
          </div>
          <br />
        </div>
      </v-col>
    </v-row>
    <!-- 회원가입 완료 모달 -->
    <SignupCompleteModal :showModal="showModal" @close="showModal = false" />
    </v-container>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import StepOne from '../components/signup/stepOne.vue';
import StepTwo from '../components/signup/stepTwo.vue';
import SignupCompleteModal from "../components/signup/signupCompleteModal.vue";

const currentStep = ref(1);
const verifiedData = ref(null);
const showModal = ref(false);

const currentComponent = computed(() => {
  return currentStep.value === 1 ? StepOne : StepTwo;
});

const handleVerified = (data: any) => {
  verifiedData.value = data;
  currentStep.value = 2;
};

</script>

<style scoped>
.v-container {
  padding: 0;
  max-width: 600px;
}

.join-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.join-row {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  margin: 0px;
}

.join-col {
  padding: 0px;
}

.join-wrapper {
  position: relative;
  /* padding-top: 2.5rem; */
}

.join-title {
  font-size: clamp(2rem, 2vw, 2.8rem);
  font-weight: 600;
  color: var(--darkText);
  text-align: center;

  margin-bottom: 2.4rem;
}

.step-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 1.6rem;
  gap: 8px;
}

.step {
  width: 1.2rem;
  height: 1.2rem;
  border-radius: 50%;
  background-color: #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  color: white;
}

.step.active {
  background-color: var(--mainColor);
}

.custom-btn {
  background-color: var(--mainColor);
  /* height: clamp(40px, 4vw, 48px); */
  height: fit-content;
  padding: 12px 24px;
  color: #fff;
  border-radius: 12px;
  box-shadow: none;
  font-size: clamp(1.4rem, 2vw, 1.6rem);
  letter-spacing: -0.025rem;

  text-transform: uppercase;
  transition: all 0.3s;
}

.custom-btn:hover {
  background-color: var(--mainHover);
}

.guide-text {
  margin-top: 1.6rem;
  font-size: 1.4rem;
  color: var(--semiDarkText);
}

.custom-link {
  color: var(--darkText);
  font-weight: 600;
}

.custom-link:hover {
  text-decoration: none;
}

.v-card-text {
  padding: 0px;
}

:deep(.rounded-input .v-input__control) {
  height: clamp(40px, 4vw, 48px) !important;
  min-height: clamp(40px, 4vw, 48px) !important;
  display: flex;
  align-items: center;
  padding: 0px;
}

:deep(.rounded-input .v-label) {
  font-size: clamp(1.4rem, 1vw, 1.4rem) !important;
}

:deep(.rounded-input input::placeholder) {
  font-size: clamp(1.4rem, 1vw, 1.4rem) !important;
  color: var(--semiDarkText);
}

:deep(.rounded-input .v-icon) {
  font-size: clamp(1.4rem, 1.2vw, 1.6rem);
}

:deep(.rounded-input .v-field__input) {
  font-size: clamp(1.4rem, 0.8vw, 1.6rem) !important;
  display: flex;
  align-items: center;
  border-radius: 12px;
  padding:0px 16px;
}

/* 테두리 둥글게 */
:deep(.rounded-input .v-field__outline) {
  border-radius: 12px;
}

:deep(.v-text-field .v-input__details) {
  padding: 12px 0px 0px 8px;
}

:deep(.v-text-field .v-input__details .v-messages) {
  font-size: 1.4rem;
  color: #FF4E4E;
  text-align: left;

  opacity: 1;
}
</style>
