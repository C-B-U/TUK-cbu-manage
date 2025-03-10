<template>
    <v-container class="first-guide-page">
        <v-row align="stretch" justify="center">
            <!-- ✅ 기본 비밀번호 & 이메일 없을 때: 둘 다 표시 -->
            <template v-if="userStore.isDefaultPassword && userStore.isEmailNull">
                <v-col cols="5" class="guide-section">
                    <GuidePassword />
                </v-col>

                <v-col cols="1" class="divider-container">
                    <div class="divider"></div>
                </v-col>

                <v-col cols="5" class="guide-section">
                    <AddMail @emailUpdated="handleEmailUpdated" />
                </v-col>
            </template>

            <!-- ✅ 이메일 존재하지만 기본 비밀번호일 때: 비밀번호 변경만 표시 -->
            <template v-else-if="userStore.isDefaultPassword">
                <v-col cols="8" class="guide-section">
                    <GuidePassword />
                </v-col>
            </template>

            <!-- ✅ 비밀번호는 바뀌었지만 이메일이 없을 때: 이메일 등록만 표시 -->
            <template v-else-if="userStore.isEmailNull">
                <v-col cols="8" class="guide-section">
                    <AddMail @emailUpdated="handleEmailUpdated" />
                </v-col>
            </template>

            <!-- ✅ 비밀번호가 바뀌었고 이메일도 있을 때: 이메일 정보 표시 -->
            <template v-else>
                <v-col cols="8" class="guide-section">
                    <div>
                        <p>이메일이 등록되어 있습니다. 변경이 불가능합니다.</p>
                    </div>
                </v-col>
            </template>
        </v-row>
    </v-container>
</template>

<script setup>
import { computed } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useRouter } from 'vue-router';
import GuidePassword from "../components/GuidePassword.vue";
import AddMail from "../components/AddMail.vue";

const router = useRouter();
const userStore = useUserStore();

const isDefaultPassword = computed(() => userStore.password === '12345678');
const isEmailNull = computed(() => !userStore.email);

const handleEmailUpdated = () => {
    // 이메일 등록/업데이트 완료 후 현재 페이지 유지
    router.go(0); // 현재 페이지로 새로고침
};
</script>

<style scoped>
.first-guide-page {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 16px;
    width: 100vw;
    max-width: 100%;
    box-sizing: border-box;
    min-height: 100vh;
}

.guide-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
}

.divider-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: auto;
}

.divider {
    width: 1px;
    background-color: #ccc;
    height: 90vh;
}
</style>