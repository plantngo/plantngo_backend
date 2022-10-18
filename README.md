# Plant&Go

## Dependencies

Before getting starting with development, install the following tools:

- [Docker](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/downloads)

## Getting Started

Firstly, you will need to clone a copy of the project:

```bash
git clone https://github.com/CS203-T1-Project/plantngo_backend
```

## Contributing

To contribute, you can look at the project wiki's [Contributing](https://github.com/CS203-T1-Project/plantngo_backend/wiki/Contributing) Section.

# Plant&Go

## Dependencies

Before getting starting with development, install the following tools:

- [Docker](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/downloads)

## Getting Started

Firstly, you will need to clone a copy of the project:

```bash
git clone https://github.com/CS203-T1-Project/plantngo_backend
```

## Contributing

To contribute, you can look at the project wiki's [Contributing](https://github.com/CS203-T1-Project/plantngo_backend/wiki/Contributing) Section.

### Testing

When contributing to the source code, you will want to ensure your codes are tested properly with the provided test cases before sending a PR (or merging into any active branches).

As this project is requires multiple containers, in order to run the test cases in the `src/test/` folder, you will need to use docker compose command:

```bash
# runs all tests
docker compose exec plantngo-backend ./mvnw test
```

To run an individual class of tests or a specific test method, refer to the `run_test.sh` file on the syntax on how you can run commands in a docker container with docker compose.
