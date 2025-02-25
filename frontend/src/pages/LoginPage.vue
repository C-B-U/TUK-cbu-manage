<template>
  <v-container class="login-page">
    <v-row align-items="center" justify="center" class="login-row">
      <v-col cols="12" sm="8" md="6" lg="6">
        <div class="login-wrapper">
          <h2 class="login-title">로그인</h2>
          <v-card-text>
            <v-form @submit.prevent="login">
              <v-text-field v-model="studentId" label="아이디" placeholder="아이디를 입력하세요" required variant="outlined"
                dense></v-text-field>
              <v-text-field v-model="password" label="비밀번호" placeholder="비밀번호를 입력하세요" required variant="outlined"
                dense></v-text-field>
              <v-btn type="submit" block large class="mt-4 font-weight-bold custom-btn">
                로그인
              </v-btn>
            </v-form>
          </v-card-text>
          <h4 class="guide-text">동아리에 새로 가입하셨나요? &nbsp; <router-link to="/join" class="custom-link">회원가입</router-link></h4>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from "vue";
import { useLogin } from "@/hooks/useLogin";

const studentId = ref("");
const password = ref("");
const { handleLogin, errorMessage, isLoggedIn, userInfo } = useLogin();

const login = async () => {
  await handleLogin({ studentId: studentId.value, password: password.value });

  if (isLoggedIn.value) {
    alert(`✅ 로그인 성공! ${userInfo.value}님 환영합니다.`);
  } else {
    alert(`❌ 로그인 실패: ${errorMessage.value}`);
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

.login-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
  padding: 16px;
  color: #333;
  text-align: start;
}

.guide-text {
  margin-top: 30px;
}

.custom-link {
  color: black;
  font-weight: bold;
  text-decoration: none;
}

.custom-link:hover {
  text-decoration: none;
}

.custom-btn {
  background-color: #515151;
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
</style>
