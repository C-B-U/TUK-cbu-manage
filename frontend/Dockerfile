FROM nginx:stable-alpine

# Node.js 설치
RUN apk add --no-cache nodejs npm

WORKDIR /app

# 환경변수 설정
ENV VITE_API_URL=/api/v1

# .env 파일이 있는 경우에만 복사 (선택적)
COPY .env* ./

# Copy package files
COPY package*.json ./
RUN npm install

# Copy source code
COPY . .
RUN npm run build

# Copy built files to nginx directory
RUN cp -r dist/* /usr/share/nginx/html/

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
