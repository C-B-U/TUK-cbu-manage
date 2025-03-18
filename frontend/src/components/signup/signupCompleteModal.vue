<template>
    <v-dialog :model-value="props.showModal" max-width="550px" persistent
        @update:model-value="emit('update:showModal', $event)">
        <v-card class="custom-card">
            <!-- 헤더 -->
            <v-card-title class="text-center font-weight-bold text-h5">활동상태 변경</v-card-title>
            <v-card-text class="text-center text-body-1">
                씨부엉 동아리의 회원이 되신 것을 축하드립니다!
            </v-card-text>

            <v-container class="content-container">
                <!-- 아이디 필드 -->
                <div class="info-box">
                    <span class="label">아이디</span>
                    <span class="value">{{ transformedUserId }}</span>
                    <v-img :src="owl2" class="owl-image right-owl"></v-img> <!-- ✅ 필드에 자연스럽게 걸치도록 수정 -->
                </div>

                <!-- 비밀번호 필드 -->
                <div class="info-box">
                    <span class="label">비밀번호</span>
                    <span class="value password-value">{{ defaultPassword }}</span>
                    <v-img :src="owl1" class="owl-image left-owl"></v-img> <!-- ✅ 필드에 자연스럽게 걸치도록 수정 -->
                </div>

                <p class="text-center text-caption small-text">
                    초기 비밀번호를 빠른 시일 내 변경하는 것을 권장드립니다!
                </p>
            </v-container>

            <!-- 하단 버튼 -->
            <v-card-actions class="action-buttons">
                <v-btn outlined color="grey" class="custom-btn change-password-btn" @click="goToChangePassword">
                    비밀번호 변경
                </v-btn>
                <v-btn class="custom-btn login-btn" @click="goToLogin">
                    로그인
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits, computed } from "vue";
import { useUserStore } from "../../stores/userStore";
import { useRouter } from "vue-router";

/* ✅ 직접 import */
import owl1 from "@/assets/owl1.svg";
import owl2 from "@/assets/owl2.svg";

const userStore = useUserStore();
const router = useRouter();

const props = defineProps({
    showModal: Boolean,
});

const emit = defineEmits(["update:showModal"]);

// 아이디 변환 (cbu+학번 형식)
const transformedUserId = computed(() => `cbu${userStore.studentNumber}`);

// 초기 비밀번호 (고정값)
const defaultPassword = computed(() => "12345678");

// 비밀번호 변경 페이지 이동
const goToChangePassword = () => {
    router.push("/private");
};

// 로그인 페이지 이동
const goToLogin = () => {
    router.push("/login");
};
</script>

<style scoped>
/* 모달 스타일 */
.custom-card {
    padding: 1.5rem;
    border-radius: 16px;
    box-shadow: 0px 4px 0.8rem rgba(0, 0, 0, 0.15);
    background-color: white;
    border: 2px solid #ddd;
}

.content-container {
    padding-top: 20px;
    position: relative;
}

/* 아이디 및 비밀번호 필드 스타일 */
.info-box {
    background: #eeeeee;
    padding: 14px 16px;
    border-radius: 0.8rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    position: relative;
    z-index: 15;
    /* ✅ 필드가 올빼미보다 위에 오도록 설정 */
}

.label {
    font-weight: bold;
    color: #666;
    font-size: 0.8rem;
}

.value {
    font-size: 0.8rem;
    font-weight: bold;
    color: #333;
}

.password-value {
    font-weight: bold;
    font-size: 0.8rem;
}

/* 하단 버튼 스타일 */
.action-buttons {
    display: flex;
    justify-content: space-between;
    padding: 16px;
}

.custom-btn {
    flex: 1;
    height: 44px;
    font-size: 0.8rem;
    font-weight: bold;
    border-radius: 8px;
}

/* 비밀번호 변경 버튼 */
.change-password-btn {
    color: #555;
    border: 1px solid #999;
}

/* 로그인 버튼 */
.login-btn {
    background-color: #4caf50;
    color: #ffffff;
    font-weight: bold;
}

/* 🦉 올빼미 이미지 스타일 */
.owl-image {
    width: 60px;
    height: auto;
    position: absolute;
    top: 50%;
    /* ✅ 필드 중앙에 맞추기 */
    transform: translateY(-40%);
    z-index: 0;
    /* ✅ 필드 뒤에 배치 */
    opacity: 1;
}

/* 아이디 필드 올빼미 (오른쪽 위) */
.right-owl {
    right: -15px;
    /* ✅ 필드 오른쪽에 살짝 걸치게 */
    top: 40px;
    /* ✅ 필드 중앙보다 살짝 아래 */
    transform: rotate(10deg);
}

/* 비밀번호 필드 올빼미 (왼쪽 아래) */
.left-owl {
    left: -20px;
    top: 100px;
    transform: rotate(0deg);
}

/* 작은 글씨 */
.small-text {
    font-size: 0.8rem;
    color: #666;
    margin-top: 8px;
}
</style>