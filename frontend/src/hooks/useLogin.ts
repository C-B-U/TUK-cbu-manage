import { ref } from "vue";

interface LoginParams {
    studentId: string;
    password: string;
}

export function useLogin() {
    const errorMessage = ref<string | null>(null);
    const isLoggedIn = ref<boolean>(false);
    const userInfo = ref<string | null>(null);

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

        // 콘솔 출력 (요청 전 확인)
        console.log("📢 로그인 요청 시작");
        console.log("👉 서버로 보낼 studentNumber:", studentNumber);
        console.log("👉 서버로 보낼 password:", password);

        try {
            const response = await fetch(`${SERVER_URL}/v1/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json", // JSON 전송을 위한 Content-Type 추가
                    "Accept": "*/*",
                },
                body: JSON.stringify({
                    studentNumber: studentNumber,
                    password: password
                }),
            });

            // 요청 후 응답 확인
            console.log("📢 서버 응답 수신 완료");

            if (response.ok) {
                userInfo.value = await response.text(); // 응답이 JSON이 아니라면 text로 처리
                isLoggedIn.value = true;
                errorMessage.value = null;

                console.log("로그인 성공! 사용자 이름:", userInfo.value);
                alert(`로그인 성공! ${userInfo.value}님 환영합니다.`);

            } else {
                const errorData = await response.json();
                errorMessage.value = errorData.message || "로그인 실패";

                console.log("로그인 실패:", errorMessage.value);
                alert(`로그인 실패: ${errorMessage.value}`);
                isLoggedIn.value = false;
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
