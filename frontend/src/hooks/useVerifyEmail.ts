import { ref } from "vue";

export default function useVerifyEmail() {
  const emailError = ref(false); // 에러 상태
  const emailErrorMessage = ref(""); // 에러 메시지
  const isVerificationSent = ref(false); // 인증번호 입력창 표시 여부

  /**
   * 이메일 유효성 검사
   */
  const validateEmail = (email: string): boolean => {
    const validDomains = ["@tukorea.ac.kr", "@kpu.ac.kr"];
    const isValid = validDomains.some((domain) => email.endsWith(domain));

    if (!isValid) {
      emailError.value = true;
      emailErrorMessage.value =
        "이메일은 @tukorea.ac.kr 또는 @kpu.ac.kr로 끝나야 합니다.";
      return false;
    }

    emailError.value = false;
    emailErrorMessage.value = "";
    return true;
  };

  /**
   * 서버로 이메일 인증 요청 전송
   */
  const SERVER_URL = import.meta.env.VITE_SERVER_URL;

  const sendEmailToServer = async (mail: string): Promise<boolean> => {
    try {
      const encodedEmail = encodeURIComponent(mail);
      const response = await fetch(`${SERVER_URL}/v1/sendMail?address=${encodedEmail}`, {
        method: "POST",
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
   */
  const verifyCodeWithServer = async (
    email: string,
    code: string
  ): Promise<boolean> => {
    try {
      const encodedEmail = encodeURIComponent(email);
      const response = await fetch(
        `${SERVER_URL}/v1/verifyMail?address=${encodedEmail}&authCode=${code}`,
        {
          method: "POST",
        }
      );

      if (!response.ok) {
        throw new Error("인증번호 검증 실패.");
      }

      const result = await response.json();
      console.log("인증 성공:", result);
      return true; // 성공 시 true 반환
    } catch (error) {
      console.error("서버 요청 에러:", error);
      emailError.value = true;
      emailErrorMessage.value = "인증번호 검증에 실패했습니다. 다시 시도해주세요.";
      return false; // 실패 시 false 반환
    }
  };

  return {
    emailError,
    emailErrorMessage,
    isVerificationSent,
    validateEmail,
    sendEmailToServer,
    verifyCodeWithServer,
  };
}
