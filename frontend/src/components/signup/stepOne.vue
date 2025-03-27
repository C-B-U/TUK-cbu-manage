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
    <v-row class="row-fix" align-items="center" justify="space-between">
      <v-col cols="9">
        <v-text-field
          class="rounded-input"
          v-model="nickName"
          label="지원 시 닉네임"
          placeholder="지원 시 구글폼에 입력한 본인의 닉네임을 적어주세요"
          variant="outlined"
          dense
          hide-details
        ></v-text-field>
      </v-col>
      <v-col cols="3" class="pa-0 d-flex align-center">
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
const nickName = ref('');

const emit = defineEmits<{ (e: 'verified', data: UserInfo): void }>();

const { verifyUser } = useVerifyUser();
const userStore = useUserStore();

const handleUserVerification = async () => {
  const result = await verifyUser(studentNumber.value, nickName.value);
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
    width: 100%;
    height: clamp(40px, 4vw, 48px);
    color: #fff;
    border-radius: 10px;
    box-shadow: none;
    font-size: clamp(0.7rem, 0.9vw, 0.9rem);
    text-transform: uppercase;
    transition: transform 0.2s ease;
    letter-spacing: 0;
    white-space: nowrap;
}

.row-fix {
  flex-wrap: wrap;
  overflow-x: hidden;
  width: 100%;
  max-width: 100%;
}

/* 입력창 컨트롤 높이와 정렬 맞춤 */
:deep(.rounded-input .v-input__control) {
  height: clamp(40px, 4vw, 48px) !important;
  min-height: clamp(40px, 4vw, 48px) !important;
  display: flex;
  align-items: center;
  padding: 0 10px;
}

/* 입력창 내부 글자 영역 */
:deep(.rounded-input .v-field__input) {
  font-size: clamp(0.65rem, 0.9vw, 0.8rem) !important;
  margin: 0;
  height: 100%;
  line-height: 1;
  display: flex;
  align-items: center;
  padding-left: 20px;
}

/* 라벨 (위에 뜨는 '학번' 글씨) */
:deep(.rounded-input .v-label) {
  font-size: clamp(0.7rem, 0.9vw, 0.9rem) !important;
  line-height: 1.2;
}

/* placeholder 크기 줄이기 */
:deep(.rounded-input input::placeholder) {
  font-size: clamp(0.7rem, 0.9vw, 0.9rem) !important;
  color: #aaa;
  line-height: 1;
}

/* 테두리 둥글게 */
:deep(.rounded-input .v-field__outline) {
  border-radius: 10px;
}

</style>
