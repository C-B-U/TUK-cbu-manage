<template>
  <v-container class="join-page">
    <v-row align-items="center" justify="center" class="join-row">
      <v-col cols="10" sm="7" md="7" lg="7">
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
.join-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 1rem;
  box-sizing: border-box;
}

.join-row {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
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
  height: 2.5rem;
  color: #fff;
  border-radius: 5px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  font-size: 1rem;
  text-transform: uppercase;
  transition: transform 0.2s ease;
}

.guide-text {
  margin-top: 1.25rem;
  font-size: 0.9rem;
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
