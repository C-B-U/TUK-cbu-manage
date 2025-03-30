<script setup lang="ts"></script>

<template>
    <div class="wrap">
        <div class="contents">
            <div class="page-title"><span style="color:var(--mainColor); font-weight:600;">씨부엉</span>에 오신 것을 환영합니다.</div>
            <p>한국공학대학교 프로그래밍 동아리 씨부엉의 공식 웹사이트입니다</p>

            <!-- 지원기간에 따라 status 변경 active/disabled -->
            <Button id="recruitment-button" status="disabled"></Button>
        </div>
    </div>

</template>

<script>
import Button from '@/components/shared/Button.vue';

export default {
    components: {
        Button,
    },
};
</script>

<style scoped>
.wrap {
    display: flex;
    justify-content: center;
    align-items: center;

    min-height: 100vh;
    background-image: url('@/assets/main-background.png');
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
}

.page-title {
    font-size: 2.4rem;
    font-weight: 600;
    color: var(--darkText);
}

.contents p {
    font-size: 1.6rem;
    color: var(--semiDarkText);

    margin: 8px 0px 36px 0px;
}
</style>
