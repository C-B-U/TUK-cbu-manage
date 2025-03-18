<script setup lang="ts"></script>

<template>
    <div class="wrap">
        <div class="contents">
            <h1 id="title"><span style="color:var(--mainColor)">씨부엉</span>에 오신 것을 환영합니다.</h1>
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

.contents #title {
    font-size: 32px;
    font-weight: 600;
}

.contents p {
    font-size: 16px;
    color: var(--semiDarkText);

    margin: 8px 0px 36px 0px;
}
</style>
