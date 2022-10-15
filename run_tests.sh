# note that you might have to run the following command to use this script 
# chmod +x run_tests.sh

# runs all tests
docker compose exec plantngo-backend ./mvnw test

# runs only one class of tests
# docker compose exec plantngo-backend ./mvnw -Dtest=<ExampleServiceTest> test

# runs only one method of a specified class of tests
# docker compose exec plantngo-backend ./mvnw -Dtest=<ExampleServiceTest>#<exampleMethod> test