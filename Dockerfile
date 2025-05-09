# Etapa de build (gera o JAR com Maven)
FROM docker.io/library/maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Cache otimizado: copiar o pom.xml e baixar dependências primeiro - Melhora cache de build — evita baixar dependências toda vez
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do projeto e realiza o build - Garante que todas as configs, YAMLs, etc. sejam incluídas
COPY . .
RUN mvn clean package -DskipTests

# Etapa final (imagem mais leve com apenas o JAR)
FROM docker.io/library/eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR do build anterior - Copia apenas o artefato final da primeira imagem
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
