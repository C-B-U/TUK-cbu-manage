import { ref } from "vue";
import { useUserStore } from "../stores/userStore";

interface LoginParams {
    studentId: string;
    password: string;
}

export function useLogin() {
    const errorMessage = ref<string | null>(null);
    const isLoggedIn = ref<boolean>(false);
    const userStore = useUserStore();

    const handleLogin = async ({ studentId = "", password = "" }: LoginParams): Promise<void> => {
        const SERVER_URL = import.meta.env.VITE_SERVER_URL;

        if (!studentId || !password) {
            errorMessage.value = "아이디와 비밀번호를 입력하세요.";
            alert(errorMessage.value);
            return;
        }

        const studentNumber = Number(studentId.replace(/^cbu/, ""));

        try {
            const response = await fetch(`${SERVER_URL}/v1/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    studentNumber,
                    password,
                }),
            });

            if (response.ok) {
                const result = await response.json();

                // email 값이 "null"(string)으로 올 경우, null로 변환
                const emailValue = result.email === "null" ? null : result.email;

                // ✅ 로그인 상태 업데이트 (role 추가)
                userStore.setUser({
                    name: result.name,
                    studentNumber,
                    email: emailValue, // email 값을 반영
                });

                userStore.setAuthStatus({
                    isDefaultPassword: password === "12345678",
                    isEmailNull: emailValue === null,
                });

                isLoggedIn.value = true;
                errorMessage.value = null;

                console.log("✅ 로그인 성공!", {
                    name: result.name,
                    studentNumber,
                    isAdmin: userStore.isAdmin,
                    isDefaultPassword: userStore.isDefaultPassword,
                    isEmailNull: userStore.isEmailNull,
                });
            }
        } catch (error) {
            errorMessage.value = "로그인 중 오류가 발생했습니다.";
            isLoggedIn.value = false;
        }
    };

    return {
        errorMessage,
        isLoggedIn,
        handleLogin,
    };
}
