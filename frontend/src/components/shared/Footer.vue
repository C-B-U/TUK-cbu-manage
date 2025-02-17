<template>
    <footer>
        <router-link to="/">
            <img id="footer-logo" src="@/assets/footer-logo.png" alt="로고이미지" />
        </router-link>

        <div class="footer-info-area">
            <p class="footer-info" v-for="(info, index) in footerInfo" :key="index">
                {{ info.title }} ㅣ {{ info.name }}
            </p>
        </div>

        <div class="footer-bottom">
            <div class="social-area">
                <a v-for="(link, index) in socialLinks" :key="index" :href="link.url">
                    <img v-if="link.icon" :src="link.icon" alt="소셜아이콘" class="social-icon" />
                </a>
            </div>
            <hr />
            <p class="copyright">
                Copyright ⓒ 2025 씨부엉 All rights reserved.
            </p>
        </div>

    </footer>
</template>

<script setup>
import instagramIcon from '@/assets/instagram.svg';
import githubIcon from '@/assets/github.svg';
import { ref } from 'vue';

// 연락처 정보 데이터
const footerInfo = [
    {
        title: '소속',
        name: '한국공학대학교',
    },
    {
        title: '문의',
        name: 'ray@tukorea.ac.kr',
    },
];

// 소셜 링크 데이터
const socialLinks = [
    { name: '인스타그램', url: 'https://www.instagram.com/tukorea_cbu', icon: instagramIcon },
    { name: '깃허브', url: 'https://github.com/C-B-U/TUK-cbu-manage', icon: githubIcon },
];
</script>

<style scoped>
footer {
    width: 100%;
    padding: 48px var(--entirePadding);
    background-color: #555555;

    display: flex;
    flex-direction: column;
    gap: 36px;
}

#footer-logo {
    width: 132px;
    height: auto;
}

footer .footer-info-area {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.footer-info-area p {
    text-align: center;
    font-size: 14px;
    color: white;
}

.footer-bottom {
    display: flex;
    flex-direction: column;
    align-items: center;

    gap: 12px;
}

.social-area {
    display: flex;
    gap: 16px;
    justify-content: center;
}

.social-icon {
    width: 24px;
    height: 24px;
    display: flex;
}

.footer-bottom > hr {
    border: none;
    width: 100%;
    height: 1px;
    background-color: var(--semiDarkText);
}

.footer-bottom .copyright {
    font-size: 12px;
    color: #CCCCCC;
}
</style>
