<template>
  <v-form @submit.prevent="handleUserVerification">
    <!-- 학번 입력 -->
    <v-text-field
      class="rounded-input"
      v-model="studentNumber"
      label="학번"
      placeholder="학번을 입력하세요"
      required
      variant="outlined"
      dense
    ></v-text-field>

    <!-- 닉네임 입력 + 합격자 확인 버튼 -->
    <v-row align-items="center" justify="space-between">
      <v-col cols="9">
        <v-text-field
          class="rounded-input"
          v-model="nickname"
          label="지원 시 닉네임"
          placeholder="지원 시 구글폼에 입력한 본인의 닉네임을 적어주세요"
          variant="outlined"
          dense
          hide-details
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-btn class="custom-btn" block type="submit">
          합격자 확인
        </v-btn>
      </v-col>
    </v-row>
  </v-form>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import useVerifyUser, { UserInfo } from '../../hooks/useVerifyUser';
import { useUserStore } from '../../stores/userStore';

// 학번과 닉네임 입력값 관리
const studentNumber = ref('');
const nickname = ref('');

const emit = defineEmits<{ (e: 'verified', data: UserInfo): void }>();

const { verifyUser } = useVerifyUser();
const userStore = useUserStore();

const handleUserVerification = async () => {
  const result = await verifyUser(studentNumber.value, nickname.value);
  if (result) {
    // 반환된 사용자 정보를 Pinia 스토어에 저장
    userStore.setUser(result);
    emit('verified', result);
  } else {
    alert('합격자 인증에 실패했습니다. 다시 시도해주세요.');
  }
};
</script>

<style scoped>
.email-btn-col {
  display: flex;
  align-items: flex-start;
}

.custom-btn {
    background-color: var(--mainColor);
    height: 56px;
    color: #fff;
    border-radius: 10px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 1rem;
    text-transform: uppercase;
    transition: transform 0.2s ease;
    letter-spacing: 0;
}

::deep .rounded-input .v-field__outline {
  border-radius: 10px;
}
</style>
