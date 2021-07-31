# drugs-app

## Testing the application

Tests (both unit and integration) can be run using Gradle:
<code>./gradlew test</code>

## Running the application

### Docker

The application and its dependencies can be started via docker-compose:
1. <code>./gradlew build</code>
2. <code>docker-compose up -d --build</code>

The application image is built from source, so in case of any modifications to the code remember to re-run 
the above steps.

### Local

Alternatively the application can be started outside of docker environment with the *local* spring profile:

<code>./gradlew bootRun --args='--spring.profiles.active=local'</code>

In such case, a mongodb instance needs to be up and running locally on port 27017 (default mongodb port).
Mongodb can be supplied from the docker-compose env:

<code>docker-compose up -d mongodb</code>

## Using the service

After launching, the app listens for requests on port 8080. In case of docker, the port 8080 is also exposed to the host.
Swagger-UI can be accessed locally via: http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config