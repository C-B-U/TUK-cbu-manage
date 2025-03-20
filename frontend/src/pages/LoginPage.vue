<template>
  <v-container class="login-page">
    <v-row align-items="center" justify="center" class="login-row">
      <v-col cols="10" sm="7" md="7" lg="8">
        <div class="login-wrapper">
          <h2 class="login-title">로그인</h2>
          <v-card-text>
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
  padding: 1rem;
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
  margin-bottom: 15px;
}

.custom-btn {
  background-color: var(--mainColor);
  height: 50px;
  color: #fff;
  border-radius: 1rem;
  box-shadow: none;
  font-size: clamp(0.875rem, 1vw, 1rem);
  text-transform: uppercase;
  transition: transform 0.2s ease;
  letter-spacing: 0;
}

.custom-btn:hover {
  transform: scale(1.02);
}

.guide-text {
  margin-top: 1.25rem;
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
  min-height: clamp(40px, 4vw, 50px) !important;
}

:deep(.rounded-input input::placeholder) {
  font-size: clamp(0.5rem, 0.7vw, 0.875rem) !important;
  color: #aaa;
}

:deep(.rounded-input .v-icon) {
  font-size: clamp(1rem, 1.2vw, 1.25rem);
}

.rounded-input .v-field__input {
  font-size: clamp(0.5rem, 0.7vw, 1rem) !important;
}
</style>
