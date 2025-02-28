import { createWebHistory, createRouter } from "vue-router";

import Main from "../pages/Main.vue";
import MemManagePage from "../pages/MemberManagePage/MemManagePage.vue";
import LoginPage from "../pages/LoginPage.vue";
import JoinPage from "../pages/JoinPage.vue";
import GuidePage from "../pages/FirstGuidePage.vue"

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
  {
    path: "/private",
    name: "guide",
    component: GuidePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;