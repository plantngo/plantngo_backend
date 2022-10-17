# Plant&Go Backend
[![Production Tests](https://github.com/plantngo/plantngo_backend/actions/workflows/production-test-suite.yml/badge.svg?branch=main&event=push)](https://github.com/plantngo/plantngo_backend/actions/workflows/production-test-suite.yml)<br>[![Production ECS Deployment](https://github.com/plantngo/plantngo_backend/actions/workflows/production-ecs-deployment.yml/badge.svg?branch=main&event=push)](https://github.com/plantngo/plantngo_backend/actions/workflows/production-ecs-deployment.yml)

## Dependencies
Before getting starting with development/running the application, install the following tools:
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/downloads)

## Getting Started
To start, make a copy of the repository with the `git clone` command or download the repository by clicking, `Code -> Download Zip` on the Github Repository Page.
```bash
git clone https://github.com/CS203-T1-Project/plantngo_backend
```

After cloning the project, you will need to `cd` into the project directory and use `docker compose` to start the project. Open up your terminal and run the following commands:
```bash
# change directory into the project
cd plantngo_backed
# start the project with docker compose
docker compose up
```

Note that this project uses the following ports on your localhost:
- `:5432`
- `:8080`

If you have an instance of PostgreSQL Server installed or have other web/system apps running on them, you might need to stop the apps running on the respective ports.

## Contributing
To contribute, you can look at the project wiki's [Contributing](https://github.com/CS203-T1-Project/plantngo_backend/wiki/Contributing) Section.

### Testing

When contributing to the source code, you need to ensure your codes are tested properly with the provided test cases before sending a `Pull-Request` (or merging into any active branches).

If you've created any new classes, it is also expected that you provide sufficient Unit Tests and Integration Tests relating to the classes created.

To run the tests, you can use the following commands:
```java
# runs all test in the project
./mvnw test
```
```java
# runs only one class of tests
./mvnw -Dtest=<ExampleServiceTest> test
```
```
# runs only one method of a specified class of tests
./mvnw -Dtest=<ExampleServiceTest>#<exampleMethod> test
```

