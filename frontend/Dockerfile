# Build stage
FROM node:18-alpine AS build
WORKDIR /app

# .env 파일이 있는 경우에만 복사 (선택적)
COPY .env* ./

# Copy package files
COPY package*.json ./
RUN npm install

# Copy source code
COPY . .
RUN npm run build

# Production stage
<<<<<<< HEAD
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
=======
FROM nginx:stable-alpine AS production
# Copy built files from build stage
COPY --from=build /app/dist /usr/share/nginx/html
# Copy nginx configuration
>>>>>>> origin/frontend-devops
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
