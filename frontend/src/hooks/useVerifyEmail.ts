import { ref } from "vue";

export default function useVerifyEmail() {
  const emailError = ref(false);
  const emailErrorMessage = ref("");

  const verificationError = ref(false);
  const verificationErrorMessage = ref("");
  const isVerificationSent = ref(false);

  // 이메일에 "@"가 없으면 기본 도메인 추가
  const addSuffixIfMissing = (email: string): string => {
    return email.includes("@") ? email : `${email}@tukorea.ac.kr`;
  };

  const SERVER_URL = import.meta.env.VITE_SERVER_URL;

  // 인증번호 전송

  const sendEmailToServer = async (mail: string): Promise<boolean> => {
    try {
      const fullEmail = addSuffixIfMissing(mail);

      const response = await fetch(`${SERVER_URL}/api/v1/mail/send`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json",
        },
        body: JSON.stringify({ address: fullEmail }),
      });

      if (!response.ok) {
        throw new Error("서버와의 통신에 실패했습니다.");
      }

      const result = await response.json();
      console.log("📩 메일 전송 응답:", result);

      if (result.success) {
        isVerificationSent.value = true;
        return true;
      } else {
        emailError.value = true;
        emailErrorMessage.value = result.responseMessage || "메일 전송 실패";
        return false;
      }
    } catch (error) {
      console.error("❌ 서버 요청 에러:", error);
      emailError.value = true;
      emailErrorMessage.value = "서버 요청에 실패했습니다. 다시 시도해주세요.";
      return false;
    }
  };

  // 인증번호 검증
  const verifyCodeWithServer = async (
    email: string,
    code: string
  ): Promise<{ success: boolean; responseMessage: string }> => {
    try {
      const fullEmail = addSuffixIfMissing(email);

      const response = await fetch(`${SERVER_URL}/api/v1/mail/verify`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json",
        },
        body: JSON.stringify({ address: fullEmail, authCode: code }),
      });

      if (!response.ok) {
        return { success: false, responseMessage: "서버 오류가 발생했습니다." };
      }

      const result = await response.json();
      console.log("📩 인증 응답:", result);

      return {
        success: result.success,
        responseMessage: result.responseMessage || "인증 결과를 확인할 수 없습니다.",
      };
    } catch (error) {
      console.error("❌ 서버 요청 에러:", error);
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
