<template>
    <v-container class="first-guide-page">
        <v-row align="stretch" justify="center">
                <v-col cols="10" class="guide-section">
                    <GuidePassword />
                </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
.first-guide-page {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 16px;
    width: 100vw;
    max-width: 100%;
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
