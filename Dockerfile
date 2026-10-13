FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Set TiDB Cloud database connection directly so it always works on Render
ENV DB_URL="jdbc:mysql://gateway01.ap-northeast-1.prod.aws.tidbcloud.com:4000/danpss_db?sslMode=VERIFY_IDENTITY"
ENV DB_USERNAME="4QdRFBPvK8sBDJh.root"
ENV DB_PASSWORD="JzVaTpV16Zd8Pd51"

ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75.0 -Xmx384m -jar app.jar"]
