# appfuxion-techincal-test

## General Info, this project using :
1. Java JDK 11,
2. Java Spring Boot 2.7.9-SNAPSHOT version,
3. Maven.

## Library :
1. Spring Web,
2. Spring Boot DevTools,
3. Lombok,
4. Spring Data JPA,
5. H2 Database,
6. Webflux / Webclient,
7. iTextPdf,
8. MySQL Driver.

## Rules :
1. Before you run this project, you must import database from database folder in this project
(Open MySQL -> Create Database with name APPFUXION-DB, after it import the SQL file).

2. Import the postman collection from postman-collection folder in this project to your postman.

3. If you want to run this project using CMD / Terminal write this script mvn spring-boot:run on your cmd or terminal.


## I provide some services:
1. Generate Pdf (localhost:8080/api/generatePdf)
   - This API I provide for fetching data from github API and then save the data (include PDF) in the database.
   
2. Get All Data (localhost:8080/api/getAllData)
  - This API I provide for fetching data from the database(you can use the ID for download the pdf file from database).

3. Download Pdf (localhost:8080/api/downloadPdf?id=12)
  - This API I provide for download Pdf file from database, use ID from the collection GET All Data as the parameter.
  
4. Delete By Id (localhost:8080/api/doDeleteData?id=1)
  - This API I provide for delete data from the database.
  
