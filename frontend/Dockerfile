FROM node:18-alpine as build
WORKDIR /app

# .env 파일이 있는 경우에만 복사 (선택적)
COPY .env* ./

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# 정적 파일만 서빙하는 간단한 서버 사용
FROM node:18-alpine
WORKDIR /app

# serve 패키지 설치
RUN npm install -g serve

# 빌드 결과물 복사
COPY --from=build /app/dist ./dist

EXPOSE 80
CMD ["serve", "-s", "dist", "-l", "80"]
