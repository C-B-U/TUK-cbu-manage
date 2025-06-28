// API URL 설정
const getApiUrl = (): string => {
  // 환경변수가 설정되어 있으면 사용, 없으면 기본값 사용
  const envUrl = import.meta.env.VITE_API_URL;
  
  if (envUrl && envUrl !== 'undefined') {
    return envUrl;
  }
  
  // 기본값: 현재 도메인의 /api/v1
  return '/api/v1';
};

export const API_BASE_URL = getApiUrl();

// API 엔드포인트들
export const API_ENDPOINTS = {
  LOGIN: `${API_BASE_URL}/login`,
  SIGNUP: `${API_BASE_URL}/login/signup`,
  MAIL_SEND: `${API_BASE_URL}/mail/send`,
  MAIL_VERIFY: `${API_BASE_URL}/mail/verify`,
  VERIFY_USER: `${API_BASE_URL}/verify`,
  CHANGE_PASSWORD: `${API_BASE_URL}/change-password`,
} as const; 