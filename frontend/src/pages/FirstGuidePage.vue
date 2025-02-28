<template>
    <v-container class="first-guide-page">
        <v-row align="stretch" justify="center">
            <!-- 비밀번호 변경 안내 (왼쪽) -->
            <v-col cols="5" class="guide-section">
                <GuidePassword />
            </v-col>

            <!-- 세로 분리선 -->
            <v-col cols="1" class="divider-container">
                <div class="divider"></div>
            </v-col>

            <!-- 최초 메일 등록 안내 (오른쪽) -->
            <v-col cols="5" class="guide-section">
                <AddMail />
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import GuidePassword from "../components/GuidePassword.vue";
import AddMail from "../components/AddMail.vue";
</script>

<style scoped>
.first-guide-page {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 16px;
    width: 100vw;  /* 뷰포트 너비 100% */
    max-width: 100%; /* 최대 너비 제한 해제 */
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