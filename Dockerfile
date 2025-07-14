FROM azul/zulu-openjdk:21.0.7

COPY target/spring-petclinic.jar spring-petclinic.jar

RUN useradd -m petclinic
USER petclinic

ENTRYPOINT ["java", "-Dspring.ai.mcp.server.stdio=true", "-jar", "/spring-petclinic.jar"]
