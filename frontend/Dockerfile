FROM node:18-alpine as build
WORKDIR /app

# .env 파일이 있는 경우에만 복사 (선택적)
COPY .env* ./

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# 빌드 결과물을 명확히 지정
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
