# Stage 1: Build Vue frontend
FROM node:18-alpine as builder
WORKDIR /app

COPY frontend ./frontend
WORKDIR /app/frontend

RUN npm install && npm run build

# Stage 2: Serve with Nginx
FROM nginx:stable-alpine

# 인증서 복사
COPY DevOps/nginx/ssl/cert.pem /etc/nginx/ssl/cert.pem
COPY DevOps/nginx/ssl/key.pem /etc/nginx/ssl/key.pem

# Nginx 설정 복사
COPY DevOps/nginx/default.conf /etc/nginx/conf.d/default.conf

# 빌드된 정적 파일 복사
COPY --from=builder /app/frontend/dist /usr/share/nginx/html
