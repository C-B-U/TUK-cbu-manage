import { ref, nextTick } from "vue";
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

        const studentNumber = parseInt(studentId.replace(/^cbu/, ""), 10);
        if (isNaN(studentNumber)) {
            errorMessage.value = "잘못된 학번 형식입니다.";
            alert(errorMessage.value);
            return;
        }

        try {
            console.log("📢 [로그인 요청] studentNumber:", studentNumber, "password:", password);

            const response = await fetch(`${SERVER_URL}/v1/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ studentNumber, password }),
            });

            console.log("📢 [서버 응답] 상태 코드:", response.status);

            if (response.ok) {
                const result = await response.json();

                console.log("✅ [서버 응답 JSON]:", result);

                // ✅ email 값이 "null"(string)일 경우 `null`로 변환
                const emailValue = result.email === "null" ? null : result.email;

                console.log("🔍 [email 값 확인]:", emailValue);

                // ✅ Pinia 상태 업데이트
                userStore.setUser({
                    name: result.name,
                    studentNumber,
                    email: emailValue,  // ✅ email 속성 추가
                });

                userStore.setAuthStatus({
                    isDefaultPassword: password === "12345678",
                    isEmailNull: emailValue === null,
                });

                isLoggedIn.value = true;
                errorMessage.value = null;

                console.log("✅ 로그인 성공! 저장된 값:", {
                    name: result.name,
                    studentNumber,
                    email: userStore.email,
                    isDefaultPassword: userStore.isDefaultPassword,
                    isEmailNull: userStore.isEmailNull,
                });

                // ✅ `nextTick`을 사용해 상태가 반영된 후 페이지 이동
                await nextTick();
            }
        } catch (error) {
            console.error("❌ 로그인 중 오류 발생:", error);
            errorMessage.value = "서버와의 통신 중 오류가 발생했습니다.";
            alert(errorMessage.value);
            isLoggedIn.value = false;
        }
    };

    return {
        errorMessage,
        isLoggedIn,
        handleLogin,
    };
}
