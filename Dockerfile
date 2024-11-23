# 1. Base Image
FROM tomcat:10.1.30-jdk17-temurin

# 2. Build arguments
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

# 3. Set environment variables for runtime
ENV DB_URL=$DB_URL
ENV DB_USERNAME=$DB_USERNAME
ENV DB_PASSWORD=$DB_PASSWORD

# 4. Set timezone
ENV JAVA_OPTS="-Duser.timezone=Asia/Seoul"

# 5. Copy Maven build artifact to Tomcat webapps
COPY target/ROOT.war /usr/local/tomcat/webapps/

# 6. Expose application port
EXPOSE 8080

# 7. Start Tomcat
CMD ["catalina.sh", "run"]
