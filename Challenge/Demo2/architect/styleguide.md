# Spring Boot Style Guide

## 1. Project Structure

```
src/main/java/com/yourcompany/project/
    ├── config
    ├── controller
    ├── dto
    ├── entity
    ├── exception
    ├── repository
    ├── service
    └── util
```

## 2. Naming Conventions

- **Classes:** PascalCase (e.g., `UserService`)
- **Variables/Methods:** camelCase (e.g., `getUserById`)
- **Constants:** UPPER_CASE (e.g., `MAX_SIZE`)
- **Packages:** lowercase (e.g., `com.example.demo`)

## 3. Annotations

- Use `@RestController` for REST APIs.
- Use `@Service` for service classes.
- Use `@Repository` for repository classes.
- Use `@Autowired` for dependency injection (prefer constructor injection).

## 4. Exception Handling

- Use `@ControllerAdvice` for global exception handling.
- Define custom exceptions in the `exception` package.

## 5. DTOs and Entities

- Separate DTOs and Entities.
- Never expose entities directly in API responses.

## 6. Logging

- Use SLF4J (`private static final Logger logger = LoggerFactory.getLogger(ClassName.class);`)
- Log at appropriate levels: `info`, `debug`, `error`.

## 7. Configuration

- Store configuration in `application.yml` or `application.properties`.
- Use `@ConfigurationProperties` for complex configs.

## 8. Testing

- Use JUnit 5 and Mockito.
- Place tests under `src/test/java`.

## 9. Documentation

- Use JavaDoc for public classes and methods.
- Document APIs with Swagger/OpenAPI.

## 10. Code Quality

- Use Checkstyle/PMD/SonarQube for static analysis.
- Follow SOLID principles and clean code practices.
