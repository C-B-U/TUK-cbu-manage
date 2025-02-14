import { ref } from "vue";

export default function useVerifyUser() {
    const verificationError = ref(false); // 에러 상태
    const verificationErrorMessage = ref(""); // 에러 메시지

    const SERVER_URL = import.meta.env.VUE_APP_SERVER_URL;

    const verifyUser = async (
        studentId: string,
        nickname: string
    ): Promise<boolean> => {
        // 필드 값이 비어 있는지 확인
        if (!studentId || !nickname) {
            verificationError.value = true;
            verificationErrorMessage.value = "모든 필드를 입력해주세요.";
            alert("이름과 학번을 입력해주세요.");
            return false;
        }

        // 학번 길이 유효성 검사
        if (!/^\d{10}$/.test(studentId)) {
            alert("학번은 10자리 숫자여야 합니다.");
            verificationError.value = true;
            verificationErrorMessage.value = "학번은 10자리 숫자여야 합니다.";
            return false;
        }

        try {
            // 서버로 전송할 데이터
            const payload = {
                name,
                studentId,
                nickname,
            };

            // 콘솔 출력: 서버로 전송되는 데이터 확인
            console.log("서버로 전송할 데이터:", payload);

            // 서버 요청
            const response = await fetch(`${SERVER_URL}/users/verify`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            // 서버 응답 처리
            if (response.ok) {
                alert("인증 완료 되었습니다! \n이어서 회원가입을 해주세요!");
                verificationError.value = false;
                verificationErrorMessage.value = "";
                return true;
            } else {
                alert("인증에 실패하였습니다. \n다시 한 번 확인해주세요.");
                verificationError.value = true;
                verificationErrorMessage.value = "서버 인증 실패.";
                return false;
            }
        } catch (error) {
            console.error("서버 요청 에러:", error);
            alert("인증에 실패하였습니다. \n다시 한 번 확인해주세요.");
            verificationError.value = true;
            verificationErrorMessage.value = "네트워크 오류가 발생했습니다.";
            return false;
        }
    };

    return {
        verificationError,
        verificationErrorMessage,
        verifyUser,
    };
}
