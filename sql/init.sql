CREATE USER sonar;
ALTER ROLE sonar WITH PASSWORD 'sonar';
CREATE DATABASE sonar;
GRANT ALL PRIVILEGES ON DATABASE sonar TO sonar;