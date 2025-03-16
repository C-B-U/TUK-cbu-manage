import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
    state: () => ({
        name: "",
        studentNumber: 0,
        email: null as string | null,
        isAdmin: false,
        isDefaultPassword: false,
        isEmailNull: true,
        emailUpdated: false,
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
        updateEmail(newEmail: string) {
            this.email = newEmail;
            this.isEmailNull = false;
            this.emailUpdated = true;
        },
        clearUser() {
            this.name = "";
            this.studentNumber = 0;
            this.email = null;
            this.isAdmin = false;
            this.isDefaultPassword = false;
            this.isEmailNull = true;
            this.emailUpdated = false; // ✅ 로그아웃 시 초기화
        },
    },
});
