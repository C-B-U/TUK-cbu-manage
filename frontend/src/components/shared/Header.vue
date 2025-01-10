<template>
    <header>
        <div id="header-nav">
            <router-link to="/">
                <img id="logo" src="@/assets/logo.png" alt="로고이미지" />
            </router-link>

            <nav>
                <ul>
                    <li v-for="item in navItems" :key="item.path">
                        <router-link :to="item.path">{{ item.name }}</router-link>
                    </li>
                </ul>
            </nav>
        </div>

        <div id="util">
            <ul>
                <li v-for="item in utilItems" :key="item.path">
                    <router-link :to="item.path">{{ item.name }}</router-link>
                </li>
            </ul>
        </div>
    </header>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 네비게이션 항목 정의
const navItems = [
    { name: '회원 관리', path: '/manage-M' },
    { name: '합격자 관리', path: '/manage-SA' },
];

const utilItems = [
    { name: '로그인', path: '/login' },
    { name: '회원가입', path: '/join' },
];
</script>

<style scoped>
header {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px var(--entirePadding);
}

header a {
    display: flex;
    color: #333;
    transition: all 0.3s;
}

header a:hover {
    color: var(--mainColor);
}

#header-nav {
    display: flex;
    align-items: center;
    gap: 200px;
}

#header-nav #logo {
    width: 132px;
    height: auto;
}

#header-nav nav ul {
    display: flex;
    gap: 120px;
}

#header-nav nav ul li a {
    font-size: 16px;
    font-weight: 600;
}

#util {
    display: flex;
    gap: 24px;
}

#util ul {
    display: flex;
    gap: 24px;
}

#util a {
    font-size: 14px;
    font-weight: 400;
}
</style>
