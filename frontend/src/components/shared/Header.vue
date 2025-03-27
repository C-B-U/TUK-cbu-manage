<template>
    <header :class="{ 'absolute-header': !isBlockHeader }">
        <div id="header-nav">
            <router-link to="/">
                <img id="logo" src="@/assets/logo.png" alt="로고이미지" />
            </router-link>

            <div id="util">
                <ul v-if="!isLoggedIn">
                    <li v-for="item in utilItems" :key="item.path">
                        <router-link :to="item.path">{{ item.name }}</router-link>
                    </li>
                </ul>

                <!-- 로그인 상태일 때 드롭다운 버튼 -->
                <div v-else class="user-menu">
                    <p class="welcome-text">{{ userStore.name }}님, 환영합니다!</p>
                    <i @click="toggleDropdown" class="dropdown-btn fas"
                        :class="isDropdownOpen ? 'fa-solid fa-caret-up' : 'fa-solid fa-caret-down'">
                    </i>

                    <!-- 드롭다운 메뉴 -->
                    <div v-if="isDropdownOpen" class="dropdown-menu">
                        <router-link to="/change-password" >비밀번호 변경</router-link>
                        <button @click="handleLogout">로그아웃</button>
                    </div>
                </div>
            </div>
        </div>
    </header>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '../../stores/userStore';

const userStore = useUserStore();
const isLoggedIn = computed(() => !!userStore.name);
const isDropdownOpen = ref(false);

// ✅ 드롭다운 토글
const toggleDropdown = () => {
    isDropdownOpen.value = !isDropdownOpen.value;
};

// ✅ 프론트에서만 로그아웃 처리 (서버 요청 없이 캐시 삭제)
const handleLogout = () => {
    userStore.clearUser(); // ✅ Pinia 상태 초기화 및 localStorage 삭제
    alert("로그아웃 되었습니다."); // ✅ 알림 표시

    setTimeout(() => {
        window.location.href = "/"; // ✅ 강제 새로고침으로 메인 이동
    }, 0); // ✅ 즉시 실행 (0ms)
};

// 특정 URL 경로 Header Absolute Style 관리
const route = useRoute();
const isBlockHeader = computed(() => {
    const blockPaths = ['/memberManage'];
    return blockPaths.includes(route.path);
});

// 로그인 전 메뉴
const utilItems = [{ name: '로그인', path: '/login' }, { name: '회원가입', path: '/join' }];
</script>


<style scoped>
header {
    width: 100%;
}

.absolute-header {
    position: absolute;
    width: 100%;
    height: 5%;
}

#header-nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1.5rem var(--entirePadding);
}

#header-nav #logo {
    width: auto;
    height: 2rem;
}

#header-nav>nav ul,
#header-nav>#util ul {
    display: flex;
}

nav a,
#util a {
    font-size: 0.9rem;
    font-weight: 500;
    color: var(--darkText);
}

#util ul {
    gap: 1.5rem;
}

/* 사용자 드롭다운 */
.user-menu {
    display: flex;
    align-items: center;
    position: relative;
    cursor: pointer;
}

/* "님, 환영합니다!" 스타일 */
.welcome-text {
    font-size: 0.85rem;
    font-weight: 500;
    color: var(--darkText);
    margin-right: 8px;
}

/* 드롭다운 버튼 스타일 */
.dropdown-btn {
    font-size: 0.9rem;
    cursor: pointer;
    transition: transform 0.2s ease;
    color: var(--darkText);
}

/* 드롭다운 메뉴 */
.dropdown-menu {
    position: absolute;
    top: 100%;
    right: 0;
    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    width: 180px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
    padding: 10px;
    display: flex;
    flex-direction: column;
    z-index: 1000;
    text-align: left;
    margin-top: 10px;
    font-size: 0.9rem;
}

/* 메뉴 스타일 통일 */
.dropdown-menu a,
.dropdown-menu button {
    font-size: inherit !important;
    font-weight: 500;
    text-align: left;
    padding: 10px 0.875rem;
    width: 100%;
    background: none;
    border: none;
    cursor: pointer;
    display: block;
}

.dropdown-menu a:hover,
.dropdown-menu button:hover {
    background: #f0f0f0;
}
</style>
