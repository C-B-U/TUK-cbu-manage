// src/stores/userStore.ts
import { defineStore } from 'pinia';
import type { UserInfo } from '../hooks/useVerifyUser';

export const useUserStore = defineStore('user', {
    state: () => ({
        name: '',
        nickName: '',
        grade: '',
        major: '',
        phoneNumber: '',
        studentNumber: 0,
        isLoggedIn: false, // ✅ 로그인 상태 추가
    }),
    actions: {
        setUser(userInfo: UserInfo) {
            this.name = userInfo.name;
            this.nickName = userInfo.nickName;
            this.grade = userInfo.grade;
            this.major = userInfo.major;
            this.phoneNumber = userInfo.phoneNumber;
            this.studentNumber = userInfo.studentNumber;
            this.isLoggedIn = true; // ✅ 로그인 성공 처리

            // ✅ 유저 정보 localStorage에 저장
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
        },

        loadUser() {
            // ✅ localStorage에서 유저 정보 불러오기
            const savedUser = localStorage.getItem('userInfo');
            if (savedUser) {
                const userInfo: UserInfo = JSON.parse(savedUser);
                this.setUser(userInfo);
            }
        },

        clearUser() {
            // ✅ 로그아웃 처리
            this.name = '';
            this.nickName = '';
            this.grade = '';
            this.major = '';
            this.phoneNumber = '';
            this.studentNumber = 0;
            this.isLoggedIn = false;

            // ✅ localStorage에서 유저 정보 삭제
            localStorage.removeItem('userInfo');
        },
    },
});
