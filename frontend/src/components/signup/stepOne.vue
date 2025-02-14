<template>
  <v-form @submit.prevent="handleUserVerification">
    <!-- 학번 입력 -->
    <v-text-field
      class="rounded-input"
      v-model="studentId"
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
import useVerifyUser from '@/hooks/useVerifyUser';

// 학번과 닉네임 입력값 관리
const studentId = ref('');
const nickname = ref('');

// 합격자 확인을 위한 hook (원래 함수 인자가 name, studentId, nickname이었다면
// 이제 studentId와 nickname으로 수정하거나 내부에서 name을 반환하도록 수정)
const { verifyUser } = useVerifyUser();

// 부모에게 인증 성공 시 이름을 전달할 이벤트 emit
const emit = defineEmits<{ (e: 'verified', name: string): void }>();

const handleUserVerification = async () => {
  // verifyUser 함수가 성공 시 합격자의 이름(문자열)을 반환한다고 가정합니다.
  const result = await verifyUser(studentId.value, nickname.value);
  if (result && typeof result === 'string') {
    console.log('닉네임 인증 성공!');
    // 인증 성공 시 부모 컴포넌트에 이름 전달 후 2단계로 전환
    emit('verified', result);
  } else {
    console.log('닉네임 인증 실패.');
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

::v-deep .rounded-input .v-field__outline {
  border-radius: 10px;
}
</style>
