import { ref } from "vue";
import { useUserStore } from "../stores/userStore"; // Pinia Store 사용

interface LoginParams {
    studentId: string;
    password: string;
}

export function useLogin() {
    const errorMessage = ref<string | null>(null);
    const isLoggedIn = ref<boolean>(false);
    const userInfo = ref<any>(null); // JSON 파싱된 데이터 저장
    const userStore = useUserStore(); // Pinia Store 사용

    const handleLogin = async ({ studentId = "", password = "" }: LoginParams): Promise<void> => {
        const SERVER_URL = import.meta.env.VITE_SERVER_URL;

        // 입력값 검증
        if (!studentId || !password) {
            errorMessage.value = "아이디와 비밀번호를 입력하세요.";
            alert(errorMessage.value);
            return;
        }

        // "cbu" 접두사를 제거한 학번 추출
        const studentNumber = studentId.replace(/^cbu/, "");

        try {
            const response = await fetch(`${SERVER_URL}/v1/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*",
                },
                body: JSON.stringify({
                    studentNumber: studentNumber,
                    password: password
                }),
            });

            console.log("📢 서버 응답 수신 완료");

            if (response.ok) {
                const rawText = await response.text();
                let parsedData;

                // JSON 파싱 시도
                try {
                    parsedData = JSON.parse(rawText);
                } catch (error) {
                    parsedData = { name: rawText, email: null }; // JSON이 아니면 기본값 설정
                }

                // `email === "null"` 문자열을 `null`로 변환
                if (parsedData.email === "null") {
                    parsedData.email = null;
                }

                // ✅ 로그인 상태 업데이트
                userInfo.value = parsedData;
                isLoggedIn.value = true;
                errorMessage.value = null;

                // `userStore`에 `name`과 `email`만 업데이트
                userStore.setUser({
                    name: parsedData.name,
                    email: parsedData.email, // `null`이 정상적으로 반영됨
                });
                userStore.updateEmail(parsedData.email);
            }
        } catch (error: any) {
            errorMessage.value = "로그인 중 오류가 발생했습니다.";
            console.error("Login error:", error);
            alert(errorMessage.value);
            isLoggedIn.value = false;
        }
    };

    return {
        errorMessage,
        isLoggedIn,
        userInfo,
        handleLogin,
    };
}
