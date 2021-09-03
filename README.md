# Covid Vaccination Service
Covid Vacciantion service runs on the port 8080

  http://localhost:8080

## Prerequisites

What things you need to install the software and how to install them

```
- IntelliJ(Optional)
- JDK11
- Maven
- NoSqlbooster(Optional)
- Database
  - MongoDb  
  - Run the script for creating database and collection in mongodb
```
## Start the spring boot service from root folder of the project
  - mvn clean package
  - java -jar target/covid_vaccination_service-0.0.1-SNAPSHOT.jar
  Or
  - mvn spring-boot:run
  Or
  - Can import in IntelliJ aand run as main application by adding the Main file

## Diagnostics

1. Check for jdk version as it requires JDK 11
      - <terminal>> java -version
2.  Check if mongo service is on
      - mongod --dbpath <your-path>/data/db
       
## Contributing

 - Suhail Mir

