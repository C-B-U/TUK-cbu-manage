<script setup lang="ts">
    import Header from './components/shared/Header.vue';
    import Footer from './components/shared/Footer.vue';

</script>

<template>

    <Header></Header>

    <div id="app">
        <router-view />
    </div>

    <Footer></Footer>

</template>

<style scoped></style>
