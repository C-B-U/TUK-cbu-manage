<template>
  <v-container class="login-page">
    <v-row align-items="center" class="login-row">
      <v-col class="login-col">
        <div class="login-wrapper">
          <h2 class="login-title">로그인</h2>
          <v-card-text class="text-container">
            <v-form @submit.prevent="login">
              <v-text-field class="rounded-input" v-model="studentId" label="아이디" placeholder="아이디를 입력하세요" required
                variant="outlined" dense></v-text-field>
              <v-text-field class="rounded-input" v-model="password" label="비밀번호"
                :type="showPassword ? 'text' : 'password'" placeholder="비밀번호를 입력하세요" required variant="outlined" dense>
                <template v-slot:append-inner>
                  <v-icon @click="showPassword = !showPassword">
                    {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                  </v-icon>
                </template>
              </v-text-field>
              <v-btn type="submit" block large class="mt-4 custom-btn">
                로그인
              </v-btn>
            </v-form>
          </v-card-text>
          <h4 class="guide-text">씨부엉 입부를 축하합니다! 첫 로그인이라면? &nbsp;<router-link to="/join"
              class="custom-link">회원가입</router-link>
          </h4>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, computed, nextTick } from "vue";
import { useRouter } from "vue-router";
import { useLogin } from "../hooks/useLogin";
import { useUserStore } from "../stores/userStore";

const studentId = ref("");
const password = ref("");
const showPassword = ref(false);
const router = useRouter();
const userStore = useUserStore();
const { handleLogin, isLoggedIn } = useLogin();

const login = async () => {
    await handleLogin({ studentId: studentId.value, password: password.value });

    // `nextTick`으로 상태 업데이트 후 이동 처리
    await nextTick();

    if (isLoggedIn.value) {
        if (password.value === "12345678" || userStore.isEmailNull) {
            router.push("/private");
        } else {
            router.push("/");
        }
    }
};
</script>


<style scoped>
.v-container {
  padding: 0;
  max-width: 720px;
}

.login-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.login-row {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  margin: 0px;
}

.login-col {
  padding: 0px;
}

.login-wrapper {
  text-align: center;
  /* border-radius: 1rem; */
  background-color: #fff;
}

.login-title {
  font-size: clamp(2rem, 2vw, 2.8rem);
  font-weight: 600;
  margin-bottom: 2.4rem; /* 로그인 라이브러리 사용함에 따라 간격 조정 */
  color: var(--darkText);
}

.text-container {
  padding: 0px;
}

.input-field {
  width: 100%;
  margin-bottom: 0px;
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

:deep(.rounded-input .v-input__control) {
  height: clamp(40px, 4vw, 48px) !important;
  min-height: clamp(40px, 4vw, 48px) !important;
  display: flex;
  align-items: center;
}

:deep(.rounded-input .v-label) {
  font-size: clamp(1.4rem, 1vw, 1.6rem) !important;
}

:deep(.rounded-input input::placeholder) {
  font-size: clamp(1.4rem, 1vw, 1.6rem) !important;
  color: var(--semiDarkText);
}

:deep(.rounded-input .v-icon) {
  font-size: clamp(1.4rem, 1.2vw, 1.6rem);
}

:deep(.rounded-input .v-field__input) {
  font-size: clamp(1.4rem, 0.8vw, 1.6rem) !important;
  height: 100%;
  display: flex;
  align-items: center;
}

/* 테두리 둥글게 */
:deep(.rounded-input .v-field__outline) {
  border-radius: 10px;
}

</style>
