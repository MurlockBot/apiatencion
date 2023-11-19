# Utiliza la imagen oficial de OpenJDK 17 como base
FROM openjdk:17

# Expone el puerto 8080 (ajústalo según tus necesidades)
EXPOSE 8081

# Copia el archivo JAR de la aplicación al contenedor (asegúrate de ajustar el nombre del archivo JAR según tu proyecto)
COPY ./target/*.jar /app.jar

# Configura el comando de inicio de la aplicación
CMD ["java", "-jar", "/app.jar"]