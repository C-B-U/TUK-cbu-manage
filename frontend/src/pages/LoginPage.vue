<template>
  <v-container class="login-page">
    <v-row align-items="center" justify="center" class="login-row">
      <v-col cols="12" sm="10" md="10" lg="8">
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
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useLogin } from "@/hooks/useLogin";
import { useUserStore } from "@/stores/userStore";

const studentId = ref("");
const password = ref("");
const showPassword = ref(false);
const router = useRouter();
const userStore = useUserStore();
const { handleLogin, isLoggedIn, userInfo } = useLogin();

const login = async () => {
    await handleLogin({ studentId: studentId.value, password: password.value });

    console.log("🔍 로그인 후 이메일 확인:", userStore.email);
    console.log("🔑 로그인 후 비밀번호 확인:", password.value);

    if (isLoggedIn.value && (password.value === "12345678" || userStore.email === null)) {
        console.log("🚀 로그인 버튼 클릭 → /private 이동");
        router.push("/private");
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
  padding: 16px;
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
  padding: 40px;
  border-radius: 12px;
  background-color: #fff;
}

.login-title {
  font-size: 1.7rem;
  font-weight: bold;
  margin-bottom: 20px;
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
  border-radius: 12px;
  box-shadow: none;
  font-size: 1rem;
  text-transform: uppercase;
  transition: transform 0.2s ease;
  letter-spacing: 0;
}

.custom-btn:hover {
  transform: scale(1.02);
}

.guide-text {
  margin-top: 20px;
}

.custom-link {
  color: black;
  font-weight: bold;
  text-decoration: none;
}

.custom-link:hover {
  text-decoration: none;
}

::v-deep .rounded-input .v-field__outline {
  border-radius: 10px;
}
</style>