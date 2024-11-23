# 1. Base Image
FROM tomcat:10.1.30-jdk17-temurin

# 2. Set environment variables
ENV JAVA_OPTS="-Duser.timezone=Asia/Seoul"


# 3. Copy Maven build artifact to Tomcat webapps
COPY target/LuckyWeeky_server-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# 4. Expose application port
EXPOSE 8080

# 5. Start Tomcat
CMD ["catalina.sh", "run"]


## 1. 기본 이미지로 OpenJDK 선택
#FROM openjdk:17-jdk-slim
#
## 2. Tomcat 다운로드 및 설치
#ENV CATALINA_HOME /usr/local/tomcat
#ENV PATH $CATALINA_HOME/bin:$PATH
#RUN apt-get update && apt-get install -y curl file && \
#    curl -o /tmp/tomcat.tar.gz https://downloads.apache.org/tomcat/tomcat-10/v10.1.30/bin/apache-tomcat-10.1.30.tar.gz && \
#    file /tmp/tomcat.tar.gz && \
#    mkdir -p $CATALINA_HOME && \
#    tar -xvf /tmp/tomcat.tar.gz -C $CATALINA_HOME --strip-components=1 && \
#    rm /tmp/tomcat.tar.gz
#
## 3. 프로젝트 WAR 파일 복사
#COPY target/LuckyWeeky_server-0.0.1-SNAPSHOT.war $CATALINA_HOME/webapps/
#
## 4. 포트 노출
#EXPOSE 8080
#
## 5. Tomcat 실행 명령어
#CMD ["catalina.sh", "run"]
