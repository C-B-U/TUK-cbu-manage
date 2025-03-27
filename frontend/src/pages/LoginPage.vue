<template>
  <v-container class="login-page">
    <v-row align-items="center" justify="center" class="login-row">
      <v-col cols="10" sm="7" md="7" lg="7">
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
              <v-btn type="submit" block large class="mt-4 font-weight-bold custom-btn">
                로그인
              </v-btn>
            </v-form>
          </v-card-text>
          <h4 class="guide-text">씨부엉 입부를 축하합니다! 첫 로그인이라면? &nbsp; <router-link to="/join"
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
.login-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 0.5rem;
  box-sizing: border-box;
}

.login-row {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-wrapper {
  text-align: center;
  padding: 2.5rem;
  border-radius: 1rem;
  background-color: #fff;
}

.login-title {
  font-size: clamp(1rem, 2vw, 1.5rem);
  font-weight: bold;
  margin-bottom: 1.25rem;
  color: #333;
}

.input-field {
  width: 100%;
  margin-bottom: 0px;
}

.custom-btn {
  background-color: var(--mainColor);
  height: clamp(40px, 4vw, 48px);
  color: #fff;
  border-radius: 10px;
  box-shadow: none;
  font-size: clamp(0.7rem, 0.9vw, 0.9rem);
  text-transform: uppercase;
  transition: transform 0.2s ease;
  letter-spacing: 0;
}

.custom-btn:hover {
  transform: scale(1.02);
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

:deep(.rounded-input .v-input__control) {
  height: clamp(40px, 4vw, 48px) !important;
  min-height: clamp(40px, 4vw, 48px) !important;
  display: flex;
  align-items: center;
}

:deep(.rounded-input .v-label) {
  font-size: clamp(0.7rem, 0.9vw, 0.9rem) !important;
  line-height: 1.2;
}

:deep(.rounded-input input::placeholder) {
  font-size: clamp(0.7rem, 0.9vw, 0.9rem) !important;
  color: #aaa;
  line-height: 1;
}

:deep(.rounded-input .v-icon) {
  font-size: clamp(1rem, 1.2vw, 1.25rem);
}

:deep(.rounded-input .v-field__input) {
  font-size: clamp(0.65rem, 0.8vw, 0.8rem) !important;
  margin: 0;
  height: 100%;
  line-height: 1;
  display: flex;
  align-items: center;
  padding-left: 20px;
}

/* 테두리 둥글게 */
:deep(.rounded-input .v-field__outline) {
  border-radius: 10px;
}

</style>
