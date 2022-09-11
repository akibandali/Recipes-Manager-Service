# Recipes-Manager-Service

Recipe Manager Service is designed and implemented for managing recipes and searching based on some filter and free text instruction. 
It has ReST APIs for performing CRUD operations from/to the database and render the requested details as JSON response to end user. 

### Prerequisites
* [JAVA 11](https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html)
* [Apache Maven](https://maven.apache.org/)
* [MySQL](https://www.mysql.com/)
* [Git](https://git-scm.com/)

### Build Instructions
* Download code zip / `git@github.com:akibandali/Recipes-Manager-Service.git`
* Move to `Recipes-Manager-Service` and run maven build command `mvn clean package`

* After build check if it has jar in `target` `directory named as recipes-authoring-command-service-0.0.1-SNAPSHOT.jar`
### Run Instructions
* **Execution on Development profile with Embedded H2 Database**
    - In Development Mode, by default web service uses [Embedded H2 database](https://spring.io/guides/gs/accessing-data-jpa/) for persisting and retrieving recipes details.
    - Command to execute:
   ```
        java -jar target/recipes-authoring-command-service-0.0.1-SNAPSHOT.jar  --spring.profiles.active=dev --logging.level.root=INFO
   ```
    - On successfull start, one should notice log message on console `Tomcat started on port(s): 8080 (http)` and have web service listening for web requests at port 8080

* **In Production, In can use MYSQL database**
    - set the required configuration parameters in application.yaml file and create a table as per RecipeEntity
        -  server.port=required-port-number
        -  spring.profiles.active=prod
        -  spring.jpa.hibernate.ddl-auto=update
        -  spring.datasource.url=jdbc:mysql://${MYSQL_HOST:hostName}:port-Number/dbname
        -  spring.datasource.username=mysql-username
        -  spring.datasource.password=mysql-user-password
        -  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  ```
### Web Service ReST API End Points

### Web Service ReST End Points Usage and Sample Response

- **Recipe Model**
    - JSON Schema
      ```
      {
          "id": "e53930fa-8175-4c58-9262-adc110dbc570",
          "name": "pasta",
          "type": "veg",
          "chef": "Gordon Ramsay",
          "servings": 100,
          "ingredients": [
              "pasta",
              "salt",
              "cream"
          ],
          "instructions": [
              "Bake onion oven updated",
              "boil pasta"
          ]
      }
      ```
- ReST API Calls and responses
    - POST request to create new Recipe : v1/authoring/recipe
      ```
      {
          "name": "pasta",
          "type": "veg",
          "chef": "Gordon Ramsay",
          "servings": 100,
          "ingredients": [
              "pasta",
              "salt",
              "cream"
          ],
          "instructions": [
              "Bake onion oven updated",
              "boil pasta"
          ]
      }
      ```
        - Put: request to update  a Recipe : v1/authoring/recipe{id}
      ```
      {
          "id": "e53930fa-8175-4c58-9262-adc110dbc570",
          "name": "pasta",
          "type": "veg",
          "chef": "Gordon Ramsay",
          "servings": 100,
          "ingredients": [
              "pasta",
              "salt",
              "cream"
          ],
          "instructions": [
              "Bake onion oven updated",
              "boil pasta"
          ]
      }
      ```
        - Delete request to delete Recipe : v1/authoring/recipe{id}
      ```
     
      ```
        - POST request to search recipe  : v1/authoring/recipe/search
        - Get all veg recipes.
      ```
      {
           "filterConditions": [{
            "field": "type",
            "value": "veg",
            "type": "INCLUDE"
        }]
      }
      ``` 
        - Get all veg recipes with servings to 10 persons.
      ```
       {
            "filterConditions": [
        {
            "field": "servings",
            "value": 10,
            "type": "INCLUDE"
        },
         {
            "field": "type",
            "value": "veg",
            "type": "INCLUDE"
        }
        ]}
         ```
 - Get all recipes with ingredients as pasta , exclude veg and  instruction contains 'oven''.
   -v1/authoring/recipe/search?instruction='oven''   
   ```
       {
            "filterConditions": [
        {
            "field": "ingredients",
            "value": "pasta",
            "type": "INCLUDE"
        },
         {
            "field": "type",
            "value": "veg",
            "type": "EXCLUDE"
        }
        ]}
      ```


### Area of Improvements
- Implement API security by defining one api that can validate the user and generate JWT and all the calls 
  should pass the JWT in header.

