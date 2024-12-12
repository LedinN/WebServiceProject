# Step 1: Use an official JDK image as a base
FROM eclipse-temurin:17-jre

# Step 2: Set the working directory
WORKDIR /app

# Step 3: Copy the built application JAR into the container
COPY build/libs/WebServiceProject-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port your app runs on
EXPOSE 8443

# Step 5: Define the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
