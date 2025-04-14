FROM nginx:stable-alpine

WORKDIR /usr/share/nginx/html

COPY ./dist .
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN echo "VITE_API_URL=$VITE_API_URL" > .env.production

CMD ["nginx", "-g", "daemon off;"]
