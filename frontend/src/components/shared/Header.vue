<template>
    <header :class="{ 'absolute-header': !isBlockHeader }">
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

            <div id="util">
                <ul>
                    <li v-for="item in utilItems" :key="item.path">
                        <router-link :to="item.path">{{ item.name }}</router-link>
                    </li>
                </ul>
            </div>
        </div>
    </header>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';

// 특정 URL 경로 Header Absolute Style 관리
const route = useRoute();

const isBlockHeader = computed(() => {
  const blockPaths = ['/memberManage'];
  return blockPaths.includes(route.path);
});

// 네비게이션 항목 정의
const navItems = [
<<<<<<< HEAD
    { name: '회원 관리', path: '/memberManage' },
=======
    { name: '회원 관리', path: '/manage-M' },
>>>>>>> 2630be6 (feat: 로그인 페이지 퍼블리싱 및 추가 디자인 적용)
];

const utilItems = [
    { name: '로그인', path: '/login' },
    { name: '회원가입', path: '/join' },
];
</script>

<style scoped>
header {
<<<<<<< HEAD
  width: 100%;
}

.absolute-header {
  position: absolute;
=======
    position: absolute;
    width: 100%;
    height: 5%;
>>>>>>> 2630be6 (feat: 로그인 페이지 퍼블리싱 및 추가 디자인 적용)
}

#header-nav {
    display: flex;
    align-items: center;
    justify-content: space-between;

    padding: 24px var(--entirePadding);
}

#header-nav > a {
    display: flex;
}

#header-nav #logo {
    width: auto;
    height: 32px;
}

#header-nav > nav ul,
#header-nav > #util ul {
    display: flex;
}

nav a,
#util a {
    font-size: 16px;
    font-weight: 500;
    color: var(--darkText);
}

#util ul{
    gap: 24px;
}

</style>
