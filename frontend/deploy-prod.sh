#!/bin/bash

echo "========== Frontend Build Start =========="
cd $(dirname $0)

# 1. 프론트 빌드
npm run build

echo "========== Build Complete =========="

# 2. dist 서버로 복사
echo "========== Upload dist to Server =========="
scp -r dist root@49.247.174.229:/root/TUK-cbu-manage/frontend/dist

# 3. 서버에서 도커 재시작
echo "========== Docker Restart =========="
ssh root@49.247.174.229 << EOF
cd /root/TUK-cbu-manage/DevOps
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml up --build -d
EOF

echo "========== Deploy Done =========="
