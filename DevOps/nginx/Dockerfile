FROM nginx:stable-alpine

WORKDIR /etc/nginx

# SSL 인증서 복사
COPY ssl /etc/nginx/ssl

# 설정파일 복사
COPY default.conf.template /etc/nginx/conf.d/default.conf.template

CMD ["/bin/sh", "-c", "envsubst '$$SSL_CERT_PATH $$SSL_KEY_PATH' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
