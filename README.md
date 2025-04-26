# Csv File Import Assignment
___
### Spring Boot Application

#### 
- This project provides an API where users can upload a CSV file containing TED Talks data. The data will be processed and stored in a PostgreSQL database. 
- The API also allows users to retrieve TED Talks data based on various filters, including authorName, year and allows users to search based on title and authorName.

### Asssumptions
- Only csv file types are accepted.
- The csv file should contain the headers as mentioned in the project description.
- The csv file should not contain any empty rows. They will be ignored.
- Except link column all the columns are required. (Can be modified based on the business requirement)
- If there will be any wrong formatted data (like wrong date or empty likes count etc. ) in the csv file, it will be ignored and the rest of the data will be inserted into the database.
- Maximum file size of the cvs file is 20MB. (Can be modified based on the business requirement)
- Calculation of influential TedTalk speakers is based on the number of views and likes. The more views and likes a speaker has, the more influential they are
  The formula is as follows;
    (influence analysis = (views * 0.7) + (likes * 0.3))

### Areas to Improvements
- The project can be improved by adding more integration tests.
- The import file process should be done after authentication and authorization.
- Caching can be added to the project (talks by authors and talks by year etc.) to improve the performance of the application.
- searchTedTalks currently has a requestParam to search in the title and authorName. It can be improved by adding a request body to search in the title and authorName we can multiple words like => "girls in tech & Martin Reeves"

### Tech Stack
- Java 21
- Spring Boot
- Restful API
- Docker

### Prerequisites

---
- Gradle
- Docker

### Build & Run

first go to the terminal and open up the project directory. "~/assignment"

### build

./gradlew clean build

### Run tests

./gradlew test

### Run the project via docker-compose

- to run the project via docker-compose go to the project directory and run the command below (I have already pushed the image to my own docker hub account so it will be downloaded via this command from the docker hub. 
  By default it will pull the image from my docker hub account. If you want to build the image yourself you can skip this step.)


    docker-compose up -d

- to stop the project  


    docker-compose down

### Run the project locally
 1 - execute the below command in the terminal. Application needs postgres database to run. You can use docker to run the postgres database.
        


    docker run --name tedtalks -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=tedtalks -p 5432:5432 -d postgres
 2 - then execute gradle run command like below
        


    ./gradlew bootRun

### Explicitly building docker images
- I used the jib plugin to build the docker image. You can find the jib plugin in the build.gradle file.
- You can build the docker image by running the command below.
   

    ./gradlew jibDockerBuild


### API DOCUMENTATION (Swagger)

- After project runs you will be able to reach the url below where you can see the API doc.
- http://localhost:8080/swagger-ui/index.html

### Metrics

- Metrics are enabled on the actuator api. We can observe the system on production.
- http://localhost:8080/actuator

### Prometheus
http://localhost:8080/actuator/prometheus