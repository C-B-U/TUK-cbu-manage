import { ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/userStore";

interface LoginParams {
    studentId: string;
    password: string;
}

export function useLogin() {
    const errorMessage = ref<string | null>(null);
    const isLoggedIn = ref<boolean>(false);
    const userStore = useUserStore();
    const router = useRouter();

    const handleLogin = async ({ studentId = "", password = "" }: LoginParams): Promise<void> => {
        const SERVER_URL = import.meta.env.VITE_API_URL;

        if (!studentId || !password) {
            errorMessage.value = "아이디와 비밀번호를 입력하세요.";
            alert(errorMessage.value);
            return;
        }

        const studentNumber = Number(studentId.replace(/^cbu/, ""));

        try {
            const response = await fetch(`${SERVER_URL}/login`, {
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

                router.push("/");
            } else {
                // 서버 응답이 400~500 범위지만 JSON 응답을 처리할 수 있는 경우
                const errorResult = await response.json();

                if (errorResult.message) {
                    let alertMessage = "로그인 중 오류가 발생했습니다. 다시 시도해주세요.";

                    if (errorResult.message === "Invalid password") {
                        alertMessage = "비밀번호가 올바르지 않습니다.\n기억이 나지 않을 시 관리자에게 문의해주세요.";
                    } else if (errorResult.message === "Member isn't exist") {
                        alertMessage = "해당 멤버가 존재하지 않습니다.\n관리자에게 문의해주세요.";
                    }

                    errorMessage.value = alertMessage;
                    alert(alertMessage);
                    isLoggedIn.value = false;
                } else {
                    errorMessage.value = "로그인 중 오류가 발생했습니다. 다시 시도해주세요.";
                    alert(errorMessage.value);
                    isLoggedIn.value = false;
                }

                isLoggedIn.value = false;
            }
        } catch (error) {
            errorMessage.value = "로그인 중 네트워크 오류가 발생했습니다. 다시 시도해주세요.";
            alert(errorMessage.value);
            isLoggedIn.value = false;
        }
    }

    return {
        errorMessage,
        isLoggedIn,
        handleLogin,
    };
}
