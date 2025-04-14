import { ref } from "vue";

export interface UserInfo {
    successMemberId: number; // 반환은 되지만 스토어에는 저장하지 않음
    name: string;
    nickName: string;
    grade: string;
    major: string;
    phoneNumber: string;
    studentNumber: number;
}

export default function useVerifyUser() {
    const verificationError = ref(false); // 에러 상태
    const verificationErrorMessage = ref(""); // 에러 메시지

    const SERVER_URL = import.meta.env.VITE_SERVER_URL;

    const verifyUser = async (
        studentNumber: string,
        nickName: string
    ): Promise<UserInfo | null> => {
        // 필드 값이 비어 있는지 확인
        if (!studentNumber || !nickName) {
            verificationError.value = true;
            verificationErrorMessage.value = "모든 필드를 입력해주세요.";
            alert("이름과 학번을 입력해주세요.");
            return null;
        }

        // 학번 길이 유효성 검사
        if (!/^\d{10}$/.test(studentNumber)) {
            alert("학번은 10자리 숫자여야 합니다.");
            verificationError.value = true;
            verificationErrorMessage.value = "학번은 10자리 숫자여야 합니다.";
            return null;
        }

        try {
            // 서버로 전송할 데이터
            const payload = {
                studentNumber: Number(studentNumber),
                nickName,
            };

            // 서버 요청
            const response = await fetch(`${SERVER_URL}/validate`, {
                method: "Post",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });
            // 서버 응답 처리
            if (response.ok) {
                // 응답 본문을 텍스트로 읽음
                const text = await response.text();
                if (!text) {
                    alert("서버로부터 빈 응답을 받았습니다.");
                    return null;
                }
                // 응답 본문을 JSON으로 파싱
                const data: UserInfo = JSON.parse(text);
                alert("인증 완료 되었습니다! \n이어서 회원가입을 해주세요!");
                verificationError.value = false;
                verificationErrorMessage.value = "";
                return data;
            } else {
                alert("인증에 실패하였습니다. \n다시 한 번 확인해주세요.");
                verificationError.value = true;
                verificationErrorMessage.value = "서버 인증 실패.";
                return null;
            }
        } catch (error) {
            console.error("서버 요청 에러:", error);
            alert("인증에 실패하였습니다. \n다시 한 번 확인해주세요.");
            verificationError.value = true;
            verificationErrorMessage.value = "네트워크 오류가 발생했습니다.";
            return null;
        }
    };

    return {
        verificationError,
        verificationErrorMessage,
        verifyUser,
    };
}
