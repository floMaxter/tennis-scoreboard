# Tennis-scoreboard
A client server application for scoring points in a tennis match.
#### Prerequisites: Prerequisites: [Java 21](https://jdk.java.net/21/), [Maven](https://maven.apache.org/), [Hibernate](https://hibernate.org/), [H2 (in-memory SQL database)](https://www.h2database.com/html/main.html), [JUnit5](https://junit.org/junit5/), HTML/CSS for frontend

## Description
The application was implemented as part of [Sergey Zhukov's roadmap](https://zhukovsd.github.io/java-backend-learning-course/projects/simulation/).

The application is implemented on a layered MVC(S) architecture. Servlets act as controllers, queries to the database are made using Hibernate, and the database is H2.

Application functionality:

- Creating a new match
- View completed matches, search for matches by player names
- Scoring points in the current match

The application contains five pages:
### 1. Home page
![Home page](image/home_page.png)
### 2. New match page
![New match page](image/new_match_page.png)
### 3. Match score page
![Match score page](image/match_score_page.png)
### 4. Completed match page
![Completed match page](image/completed_match_page.png)
### 5. Finished matches page
![Finished matches page](image/finished_matches_page.png)

## Getting started
### 1. Clone the repository
```shell
git clone https://github.com/floMaxter/tennis-scoreboard
cd tennis-scoreboard
```
or download zip archive
https://github.com/floMaxter/tennis-scoreboard/archive/refs/heads/main.zip

### 2. Assemble it .a war file using Maven
**Note:** The Maven Wrapper is already present in the project, so use the following commands to build

* Linux/macOS:
```bash
./mvnw clean package
```
* Windows
```shell
.\mvnw clean package
```

**Note:** Ready.The war file will be located in the `target/`

### 3. Expand it `.war` in Tomcat
* Copy `target/tennis-scoreboard.war` to the `webapps` of Tomcat
* Run Tomcat
- Linux/maOS
```shell
./bin/startup.sh
```
- Windows
```shell
.\bin\startup.bat
```

### 4. Open app in browser
`http://localhost:8080/tennis-scoreboard/`
