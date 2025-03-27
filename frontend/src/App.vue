<script setup lang="ts">
import { onMounted } from 'vue';
import Header from './components/shared/Header.vue';
import Footer from './components/shared/Footer.vue';
import { useUserStore } from './stores/userStore';

onMounted(() => {
    const userStore = useUserStore();
    userStore.loadUserFromStorage();
});
</script>

<template>

    <Header></Header>

    <div id="app">
        <router-view />
    </div>

    <Footer></Footer>

</template>

<style scoped></style>
