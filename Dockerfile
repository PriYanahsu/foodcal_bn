FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Tuned for Render's free tier (512 MB, fraction of a CPU), where cold-start time is what users feel.
# The JVM's default heap there is only ~128 MB, and GC thrashing during boot can stretch it past minutes.
# C1-only JIT and serial GC trade a little peak throughput for a much faster start on a starved CPU.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Xss512k"
ENTRYPOINT ["java", "-jar", "app.jar"]
