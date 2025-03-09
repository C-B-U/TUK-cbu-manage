// src/stores/userStore.ts
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        name: '',
        nickName: '',
        grade: '',
        major: '',
        phoneNumber: '',
        studentNumber: 0,
        email: null as string | null,
        isLoggedIn: false, // ✅ 로그인 상태 추가
    }),
    actions: {
        setUser(userInfo: any) {
            this.name = userInfo.name;
            this.nickName = userInfo.nickName;
            this.grade = userInfo.grade;
            this.major = userInfo.major;
            this.phoneNumber = userInfo.phoneNumber;
            this.studentNumber = userInfo.studentNumber;
            this.isLoggedIn = true; // ✅ 로그인 성공 처리
        },
        updateEmail(newEmail: string) {
            // ✅ 이메일 업데이트 시 실행
            this.email = newEmail;
            console.log("📩 이메일 업데이트됨:", this.email);
        },

        clearUser() {
            // ✅ 로그아웃 처리
            this.name = '';
            this.nickName = '';
            this.grade = '';
            this.major = '';
            this.phoneNumber = '';
            this.email = null; // ✅ email 초기화
            this.studentNumber = 0;
            this.isLoggedIn = false;

            console.log("🚪 로그아웃: 유저 정보 삭제됨");
        },
    },
});