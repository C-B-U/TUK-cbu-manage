<template>
    <footer>
        <router-link to="/">
            <img id="footer-logo" src="@/assets/logo.png" alt="로고이미지" />
        </router-link>

        <div class="footer-info-area">
            <p class="footer-info" v-for="(info, index) in footerInfo" :key="index">
                {{ info.title }}: {{ info.name }} ㅣ 이메일: {{ info.email }} ㅣ 번호: {{ info.phone }}
            </p>
        </div>

        <div class="social-area">
            <a v-for="(link, index) in socialLinks" :key="index" :href="link.url">
                <img v-if="link.icon" :src="link.icon" alt="인스타그램" class="social-icon" />
            </a>
        </div>

        <hr />
        <p class="copyright">
            Copyright ⓒ 2025 씨부엉 All rights reserved.
        </p>
    </footer>
</template>

<script setup>
import instagramIcon from '@/assets/instagram.svg';
import { ref } from 'vue';

// 연락처 정보 데이터
const footerInfo = [
    {
        title: '회장',
        name: '정우혁',
        email: '000@tukorea.ac.kr',
        phone: '010-1234-5678',
    },
    {
        title: '부회장',
        name: '정우혁',
        email: '000@tukorea.ac.kr',
        phone: '010-1234-5678',
    },
];

// 소셜 링크 데이터
const socialLinks = [
    { name: '인스타그램', url: 'https://www.instagram.com/tukorea_cbu', icon: instagramIcon },
    { name: '한국공학대학교', url: '#' },
];
</script>

<style scoped>
footer {
    width: 100%;
    padding: 32px var(--entirePadding);
    background-color: #eeeeee;
}

#footer-logo {
    width: 132px;
    height: auto;
    mix-blend-mode: darken;
}

footer .footer-info-area {
    margin: 32px 0;
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.footer-info-area p {
    text-align: center;
    font-size: 14px;
    color: #333;
}

.social-area {
    display: flex;
    gap: 16px;
    justify-content: center;
}

.social-area a {
    font-size: 14px;
    color: #333;
}

.social-icon {
    width: 25px;
    height: 25px;
    display: inline-block;
}

footer hr {
    border: none;
    height: 1px;
    background-color: #cccccc;
    margin: 12px 0;
}

.copyright {
    font-size: 12px;
    color: #888;
}
</style>
