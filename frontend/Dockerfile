# Stage 1: Build
FROM node:18-alpine as builder
WORKDIR /app
COPY . .
RUN npm install && npm run build

# Stage 2: Serve with Nginx
FROM nginx:stable-alpine
COPY --from=builder /app/dist /usr/share/nginx/html

# Optional: remove default Nginx config if needed
RUN rm /etc/nginx/conf.d/default.conf
