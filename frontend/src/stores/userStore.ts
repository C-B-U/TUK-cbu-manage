import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
    state: () => ({
        name: "",
        studentNumber: 0,
        email: null as string | null,
        isAdmin: false,
        isDefaultPassword: false,
        isEmailNull: true,
    }),
    actions: {
        setUser(userInfo: { name: string; studentNumber: number; email?: string | null }) {
            this.name = userInfo.name;
            this.studentNumber = userInfo.studentNumber;
            this.email = userInfo.email ?? null;

            this.isAdmin = userInfo.name === "관리자" && userInfo.email === "cbuAdmin@tukorea.ac.kr";
        },
        setAuthStatus(authStatus: { isDefaultPassword: boolean; isEmailNull: boolean }) {
            this.isDefaultPassword = authStatus.isDefaultPassword;
            this.isEmailNull = authStatus.isEmailNull;
        },
        clearUser() {
            this.name = "";
            this.studentNumber = 0;
            this.email = null;
            this.isAdmin = false;  // 로그아웃 시 기본 역할로 변경
            this.isDefaultPassword = false;
            this.isEmailNull = true;
        },
    },
});
