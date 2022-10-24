# note that you might have to run the following command to use this script 
# chmod +x run_tests.sh

# runs all tests
./mvnw test

# runs only one class of tests
# ./mvnw -Dtest=<ExampleServiceTest> test

# runs only one method of a specified class of tests
# ./mvnw -Dtest=<ExampleServiceTest>#<exampleMethod> test