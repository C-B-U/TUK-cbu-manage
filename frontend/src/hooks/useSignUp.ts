import { ref } from "vue";

export default function useSignUp() {
    const signUpError = ref(false);
    const signUpErrorMessage = ref("");
    const isSignUpSuccessful = ref(false);
    const SERVER_URL = import.meta.env.VITE_SERVER_URL;

    const registerUser = async (
        email: string,
        studentNumber: number,
        name: string,
        nickname: string
    ) => {
        try {
            const payload = {
                email,
                password: "12345678", // 기본 비밀번호 1234로 고정
                name,
                studentNumber,
                nickname,
            };

            console.log("📩 서버에 보낼 데이터:", JSON.stringify(payload, null, 2));

            const response = await fetch(`${SERVER_URL}/v1/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            let result;
            if (response.ok) {
                result = await response.text(); // 200일 때는 텍스트 반환
                console.log("✅ 회원가입 성공:", result);
            } else {
                result = await response.json(); // 에러일 때는 JSON 반환
                throw new Error(result.error || "회원가입 요청 실패");
            }

            isSignUpSuccessful.value = true;
            signUpError.value = false;
            signUpErrorMessage.value = "";
        } catch (error: any) {
            signUpError.value = true;
            signUpErrorMessage.value =
                error.message || "회원가입 중 알 수 없는 오류가 발생했습니다.";
            console.error("❌ 회원가입 실패:", error);
        }
    };

    return {
        signUpError,
        signUpErrorMessage,
        isSignUpSuccessful,
        registerUser,
    };
}
