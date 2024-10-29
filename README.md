# News_Search
 The News Search Microservice allows users to search for news articles based on a keyword.

# News Search Microservice

## Overview

The News Search Microservice allows users to search for news articles based on a keyword. It fetches data from the NewsAPI and groups the results based on publication dates. Users can interact with the service via a REST API, with capabilities for viewing results in real-time or offline mode.

## Features

- Search for news articles by keyword.
- Group results by publication date intervals.
- Support for offline mode with caching.
- Comprehensive API documentation via Swagger.

## Getting Started

### Prerequisites

- Java 11 or later
- Maven
- Spring Boot

### Installation

1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn clean install` to build the project.
4. Run the application with `mvn spring-boot:run`.

### API Documentation

You can view the API documentation at [Swagger UI](http://localhost:8080/swagger-ui.html).

## API Endpoints

### `GET /api/news/search`

- **Description**: Search for news articles based on a keyword.
- **Parameters**:
  - `keyword`: The keyword to search for (required).
  - `interval`: The time interval (default is 12).
  - `intervalUnit`: The unit of time for grouping (default is "hours").
  - `offlineMode`: A boolean indicating whether to use cached articles (default is false).
  
- **Responses**:
  - `200`: Successful retrieval of articles.
  - `400`: Invalid input parameters.
  - `500`: Internal server error.

## Design Patterns Used

- **Singleton Pattern**: For the `RestTemplate` bean to ensure a single instance is used across the application.
- **Factory Pattern**: To create instances of grouped articles based on intervals.
- **Strategy Pattern**: For determining the time interval calculation method based on user input.

## Sequence Diagram

![image](https://github.com/user-attachments/assets/c8850e58-2b62-441f-a16e-3d1a04d9541c)


## Conclusion

This documentation serves as a guide for developers working on or maintaining the backend service. Feel free to contribute or report issues as necessary.

