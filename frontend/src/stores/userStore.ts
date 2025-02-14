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
    }),
    actions: {
        setUser(userInfo: UserInfo) {
            // successMemberId는 저장하지 않음
            this.name = userInfo.name;
            this.nickName = userInfo.nickName;
            this.grade = userInfo.grade;
            this.major = userInfo.major;
            this.phoneNumber = userInfo.phoneNumber;
            this.studentNumber = userInfo.studentNumber;
        },
    },
});
