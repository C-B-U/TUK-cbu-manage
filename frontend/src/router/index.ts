import { createWebHistory, createRouter } from "vue-router";

import Main from "../pages/Main.vue";
import MemManagePage from "../pages/MemberManagePage/MemManagePage.vue";
import PassMemManage from "../pages/PassMemManagePage.vue";
import TestLoginPage from "../pages/TestLoginPage.vue";
import JoinPage from "../pages/JoinPage.vue";

const routes = [
  {
    path: "/",
    name: "main",
    component: Main,
  },
  {
    path: "/manage-M",
    name: "manage-M",
    component: MemManagePage,
  },
  {
    path: "/manage-SA",
    name: "manage-SA",
    component: PassMemManage,
  },
  {
    path: "/login",
    name: "login",
    component: TestLoginPage,
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