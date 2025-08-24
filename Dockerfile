# 베이스 이미지
FROM openjdk:17-jdk-slim

# JAR 파일 복사
COPY build/libs/Redis_Practice-0.0.1-SNAPSHOT.jar app.jar


# 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
