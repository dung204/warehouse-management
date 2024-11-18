> This repository is a public archive of the project in a team of 5 members in the subject SWP391 - Software Project at FPT University. The original repository is available here: [https://gitlab.com/dunghahe181529/warehouse-management](https://gitlab.com/dunghahe181529/warehouse-management)

# Warehouse Management Application

## Tables of content

- [1. Pre-requisites](#1-pre-requisites)
- [2. Getting started](#2-getting-started)
- [3. Libraries (dependencies)](#3-libraries-dependencies)
- [4. Folder structure](#4-folder-structure)
- [5. License](#5-license)

## 1. Pre-requisites

You need to install all of these before continuing:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)
- [NodeJS](https://nodejs.org/en)
- [MySQL](https://dev.mysql.com/downloads/mysql)

## 2. Getting started

1. Clone the repository

```bash
git clone https://gitlab.com/dunghahe181529/warehouse-management.git
```

2. Change directory

```bash
cd warehouse-management
```

3. Config git to use the correct username and email

```bash
git config user.name <your-username> # replace <your-username> with your actual username on GitLab
git config user.email <your-email> # replace <your-email> with your actual email on GitLab
```

> ⚠️ **Warning**: Do not use the `--global` flag, as it will change the global configuration for all repositories.

4. Install Node.js dependencies for `Husky`, and `lint-staged` (used for running Prettier before committing)

```bash
npm ci
```

> 📝 **Note**: If formatter does not work when committing, running the above command can help

5. Create a configuration file, named `application-dev.yml`, place it at `src/resources/`. The content of this file should follow the example file ([`application-dev-example.yml`](src/main/resources/application-dev-example.yml))

6. Run the application (all the libraries will be installed automatically in first time run)

```bash
mvn spring-boot:run
```

7. Open your browser and navigate to [`http://localhost:8080`](http://localhost:8080)

## 3. Libraries (dependencies)

This project includes the following libraries:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Web](https://spring.io/guides/gs/serving-web-content/)
- [Spring Security](https://spring.io/guides/gs/securing-web/): handling authentication and authorization
- [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/): creating entities and repositories
- [Spring DevTools](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-devtools)
- [Lombok](https://projectlombok.org/): generating boilerplate code (constructor, getter, setter, etc.)
- [Thymeleaf](https://www.thymeleaf.org/): view template engine
- [MySQL Connector J](https://dev.mysql.com/doc/connector-j/8.0/en/): MySQL database driver
- [Prettier Maven Plugin](https://github.com/HubSpot/prettier-maven-plugin): code formatting
- [Husky](https://github.com/typicode/husky): managing Git hooks (require Node.js)

## 4. Folder structure

After following all the steps from [getting started section](#2-getting-started), your project folder should look like this:

```
├── .husky/
├── .mvn/
├── node_modules/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── swp391/
│   │   │           └── warehouse_management/
│   │   │               ├── common/
│   │   │               ├── configs/
│   │   │               ├── controllers/
│   │   │               ├── dtos/
│   │   │               ├── entities/
│   │   │               ├── filters/
│   │   │               ├── repositories/
│   │   │               ├── services/
│   │   │               ├── utils/
│   │   │               └── WarehouseManagementApplication.java
│   │   └── resources/
│   │       ├── template/
│   │       ├── application-dev-example.yml
│   │       ├── application-dev.yml
│   │       └── application.yml
│   └── test
├── target/
├── .gitignore
├── .prettierrc.yml
├── package.json
├── package-lock.json
├── pom.xml
└── README.md
```

- `.husky/`: contains the configuration for Husky, a tool that helps to manage Git hooks. In this project, it is used to run Prettier to format the code before committing (**<span style="color: red;">DO NOT EDIT</span>**).
- `.mvn/`: contains the configuration for Maven (**<span style="color: red;">DO NOT EDIT</span>**).
- `node_modules/`: contains all the Node.js dependencies, in this case, there's only dependencies for Husky (**<span style="color: red;">DO NOT EDIT</span>**).
- `src/main/java/com/swp391/warehouse_management/`: contains all the Java source code.
  - `common/`: contains common classes and interfaces, used in various places.
  - `configs/`: contains configuration classes, used on the startup of application.
  - `controllers/`: contains controller classes, which handle all HTTP requests and responses.
  - `dtos/`: contains DTO (Data Transfer Object) classes.
  - `entities/`: contains entity classes, which represent the schema for SQL tables.
  - `filters/`: contains filter classes, which intercept the HTTP requests and responses.
  - `repositories/`: contains repository classes, which query data from database.
  - `services/`: contains service classes, which handles business logic.
  - `utils/`: contains utility classes, which includes reusable helper function.
  - `WarehouseManagementApplication.java`: the main class (entry point) of the application.
- `src/main/resources/`: contains all the resources.
  - `template/`: contains Thymeleaf template files (in HTML).
  - `application-dev-example.yml`: an example configuration file for development environment (**<span style="color: red;">DO NOT EDIT</span>**).
  - `application-dev.yml`: the configuration file for development environment.
  - `application.yml`: the main configuration file (**<span style="color: red;">DO NOT EDIT</span>**).
- `src/test/`: contains all the test classes.
- `target/`: contains all the compiled classes and resources (**<span style="color: red;">DO NOT EDIT</span>**).
- `.gitignore`: contains the list of files and folders that should be ignored by Git (**<span style="color: red;">DO NOT EDIT</span>**).
- `.prettierrc.yml`: contains the configuration for Prettier (**<span style="color: red;">DO NOT EDIT</span>**).
- `package.json`: contains the list of Node.js dependencies (**<span style="color: red;">DO NOT EDIT</span>**).
- `package-lock.json`: contains the list of Node.js dependencies with specific version (**<span style="color: red;">DO NOT EDIT</span>**).
- `pom.xml`: contains the configuration for Maven (**<span style="color: red;">DO NOT EDIT</span>**).
- `README.md`: the file you are reading right now.

## 5. License

This project is licensed under The Unlicense - see the [LICENSE](LICENSE) file for details.
