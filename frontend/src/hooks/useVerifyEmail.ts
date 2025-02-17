import { ref } from "vue";

export default function useVerifyEmail() {
  const emailError = ref(false); // 에러 상태
  const emailErrorMessage = ref(""); // 에러 메시지

  const verificationError = ref(false); // 인증번호 에러 상태
  const verificationErrorMessage = ref(""); // 인증번호 에러 메시지
  const isVerificationSent = ref(false); // 인증번호 입력창 표시 여부

  // 이메일에 "@"가 없으면 기본 접미사 추가
  const addSuffixIfMissing = (email: string): string => {
    if (!email.includes("@")) {
      return email + "@tukorea.ac.kr";
    }
    return email;
  };

  const SERVER_URL = import.meta.env.VITE_SERVER_URL;

  const sendEmailToServer = async (mail: string): Promise<boolean> => {
    try {
      const fullEmail = addSuffixIfMissing(mail);
      const encodedEmail = encodeURIComponent(fullEmail);
      const response = await fetch(`${SERVER_URL}/v1/sendMail?address=${encodedEmail}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "accept": "*/*",
        },
      });

      if (!response.ok) {
        throw new Error("서버와의 통신에 실패했습니다.");
      }

      const result = await response.json();
      console.log("서버 응답:", result);

      isVerificationSent.value = true; // 인증번호 입력창 표시
      return true; // 성공 시 true 반환
    } catch (error) {
      console.error("서버 요청 에러:", error);
      emailError.value = true;
      emailErrorMessage.value = "서버 요청에 실패했습니다. 다시 시도해주세요.";
      return false; // 실패 시 false 반환
    }
  };

  /**
     * 서버로 인증번호 검증 요청  
     * 서버 응답의 responseMessage를 그대로 사용
     */
  const verifyCodeWithServer = async (
    email: string,
    code: string
  ): Promise<{ success: boolean; responseMessage: string }> => {
    try {
      const fullEmail = addSuffixIfMissing(email);
      const encodedEmail = encodeURIComponent(fullEmail);
      const response = await fetch(
        `${SERVER_URL}/v1/verifyMail?address=${encodedEmail}&authCode=${code}`,
        {
          method: "POST",
        }
      );

      if (!response.ok) {
        return { success: false, responseMessage: "서버 오류가 발생했습니다." };
      }

      const result = await response.json();
      console.log("인증 응답:", result);

      return {
        success: result.success,
        responseMessage: result.responseMessage || "인증 결과를 확인할 수 없습니다.",
      };
    } catch (error) {
      console.error("서버 요청 에러:", error);
      return { success: false, responseMessage: "네트워크 오류가 발생했습니다." };
    }
  };

  return {
    emailError,
    emailErrorMessage,
    verificationError,
    verificationErrorMessage,
    isVerificationSent,
    sendEmailToServer,
    verifyCodeWithServer,
  };
}
