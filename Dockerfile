FROM openjdk:11
COPY "./target/person-monolith-api.jar" "person-monolith-i.jar"
EXPOSE 7979
ENTRYPOINT ["java", "-jar", "person-monolith-i.jar"]