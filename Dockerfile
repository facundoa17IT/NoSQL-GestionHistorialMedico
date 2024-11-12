# Usamos una imagen de Maven para compilar la aplicación
FROM maven:3.8.3-openjdk-17 AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos los archivos de proyecto al contenedor
COPY pom.xml . 
COPY src ./src

# Compilamos la aplicación
RUN mvn clean package -DskipTests

# Usamos una imagen de JDK para ejecutar la aplicación
FROM openjdk:17-slim

# Establecemos el directorio de trabajo en el nuevo contenedor
WORKDIR /app

# Copiamos el JAR generado desde el contenedor de compilación
COPY --from=build /app/target/tarea2-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]