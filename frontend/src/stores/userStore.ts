import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
    state: () => ({
        name: "",
        studentNumber: 0,
        email: null as string | null, // ✅ email 속성 추가
        isDefaultPassword: false,
        isEmailNull: true,
    }),
    actions: {
        setUser(userInfo: { name: string; studentNumber: number; email?: string | null }) {
            this.name = userInfo.name;
            this.studentNumber = userInfo.studentNumber;
            this.email = userInfo.email ?? null; // ✅ email 속성 반영
        },
        setAuthStatus(authStatus: { isDefaultPassword: boolean; isEmailNull: boolean }) {
            this.isDefaultPassword = authStatus.isDefaultPassword;
            this.isEmailNull = authStatus.isEmailNull;
        },
        clearUser() {
            this.name = "";
            this.studentNumber = 0;
            this.email = null;
            this.isDefaultPassword = false;
            this.isEmailNull = true;
        },
    },
});
