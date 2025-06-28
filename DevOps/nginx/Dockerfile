FROM nginx:stable-alpine

# nginx 설정 복사
COPY default.conf.template /etc/nginx/conf.d/default.conf

# 정적 파일 복사
COPY dist/ /usr/share/nginx/html
