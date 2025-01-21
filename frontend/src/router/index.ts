import { createWebHistory, createRouter } from "vue-router";

import Main from "../pages/Main.vue";
import MemManagePage from "../pages/MemberManagePage/MemManagePage.vue";
<<<<<<< HEAD
import TestLoginPage from "../pages/TestLoginPage.vue";
=======
import PassMemManage from "../pages/PassMemManagePage.vue";
import LoginPage from "../pages/LoginPage.vue";
>>>>>>> 2630be6 (feat: 로그인 페이지 퍼블리싱 및 추가 디자인 적용)
import JoinPage from "../pages/JoinPage.vue";

const routes = [
  {
    path: "/",
    name: "main",
    component: Main,
  },
  {
    path: "/memberManage",
    name: "memberManage",
    component: MemManagePage,
  },
  {
    path: "/login",
    name: "login",
    component: LoginPage,
  },
  {
    path: "/join",
    name: "join",
    component: JoinPage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;