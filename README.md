# Covid Vaccination Service

Covid Vaccination Service is Reactive Spring Boot Microservice which is specifically return’s the data of vaccination details of each country.
The data source used is publicly available database in the github account of OWID(Our World In Data). 

- Covid Vacciantion service runs on the port 8080
  `` http://localhost:8080``

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
### Databse Scripts

  ``
    use covid_vaccination_db;
    db.createCollection("manufacturer");
    db.createCollection("vaccination");
    db.vaccination.createIndex( { "iso_code": 1 }, { unique: true } );
    db.manufacturer.createIndex( { "iso_code": 1 }, { unique: true } );

  ``
## Start the spring boot service from root folder of the project
  - mvn clean package
  - java -jar target/covid_vaccination_service-0.0.1-SNAPSHOT.jar
   * ``(Or)``
  - mvn spring-boot:run
   * ``(Or)``
  - Can import in IntelliJ aand run as main application by adding the Main file

## API
  - http://localhost:8080/covid-vaccination-service/vaccination/country/iso-code/{iso-code}
  - http://localhost:8080/covid-vaccination-service/vaccination/manufacturer/iso-code/{iso-code}
  -
  - Repalce iso-code with isocode3 of countrties like GBR,IND.
  - Example : http://localhost:8080/covid-vaccination-service/vaccination/manufacturer/iso-code/GBR

## Diagnostics

1. Check for jdk version as it requires JDK 11
      - <terminal>> java -version
2.  Check if mongo service is on
      - mongod --dbpath <your-path>/data/db
       
## Contributing

 - Suhail Mir
  
 ## References
  - https://start.spring.io/
  - https://spring.io/reactive
  
 ## Dataset
    - https://github.com/owid/covid-19-data/tree/master/public/data
