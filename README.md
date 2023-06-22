# Link Validator API

## Description
This repository contains an API designed to query the MongoDB ETL results from the [Phishing-Domain-Extractor](https://github.com/benayat/Phishing-Domain-Extractor) repository. The API is built with Spring Boot and uses Redis for caching.

## Components

### Configuration
- `RedisCacheConfig.java`: This class provides the configuration for Redis Cache. It sets the cache entry time to live (TTL) to 60 minutes and disables caching of null values.
- `RedisConnectionConfig.java`: This class provides the configuration for Redis Connection. It uses LettuceConnectionFactory to establish a connection with Redis.

### Controller
- `LinksController.java`: This class is a REST controller that exposes endpoints to validate URLs. It provides two GET endpoints, `/validateLink-url` and `/validateLink-domain`, which validate a URL based on direct search or domain search respectively.

### Service
- `LinksService.java`: This class provides the business logic for the application. It contains methods to validate a URL based on direct search or domain search.

### Repository
- `MongodbRepository.java`: This class provides the MongoDB operations for the application.

### Utility
- `UrlUtils.java`: This class provides utility functions related to URL processing.

## Redis Caching
The use of Redis for caching in this API significantly improves the performance by reducing the number of direct queries to MongoDB. When a URL is validated, the result is stored in the Redis cache. Subsequent requests for the same URL are then served from the cache, which is much faster than querying the database. This not only speeds up the response time but also reduces the load on the MongoDB server.

## Installation

1. Clone this repository to your local machine.
2. Ensure you have Docker installed and running.
3. Run MongoDB and Redis on Docker using the default ports.
4. Run the Spring Boot application.

## Usage

1. Use the `/validateLink-url` endpoint to validate a URL based on direct search.
2. Use the `/validateLink-domain` endpoint to validate a URL based on domain search.

## Contributing

Contributions are welcome. Please fork the repository and create a pull request with your changes.

## Contact
For any inquiries or issues, please contact [benaya7@gmail.com](mailto:benaya7@gmail.com).

## License

This project is licensed under the terms of the MIT license.