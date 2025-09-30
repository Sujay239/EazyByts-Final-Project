# News Aggregator

Short, focused README for the News Aggregator Spring Boot project. This document describes purpose, architecture, setup, configuration, running, testing, and deployment notes for a Java Spring Boot application.

## Overview

News Aggregator collects articles from multiple sources, normalizes content, stores results, and serves them via a Spring Boot REST API. It handles deduplication, caching, rate limits, and source-specific parsing.

## Tech stack

- Java 11+ (or your project's Java version)
- Spring Boot (Web, Data JPA, Scheduling)
- Database: Postgres/MySQL/SQLite (dev)
- Optional: Redis for caching
- Build: Maven or Gradle
- Docker (optional)

## Prerequisites

- Java JDK 11+
- Maven (or use the included mvnw) or Gradle (or the included gradlew)
- A database (Postgres recommended)
- Redis (optional)
- API keys for upstream news APIs (configured via application.properties or environment variables)

## Quick start (local)

1. Clone repository
   - git clone <repo-url>
2. Configure environment (see Configuration)
3. Build
   - With Maven: ./mvnw clean package
   - With Gradle: ./gradlew build
4. Run
   - With Maven: ./mvnw spring-boot:run
   - Or run the jar: java -jar target/\*.jar

## Configuration

Spring Boot uses application.properties / application.yml. Common properties (examples):

- server.port=8080
- spring.datasource.url=jdbc:postgresql://localhost:5432/newsdb
- spring.datasource.username=dbuser
- spring.datasource.password=secret
- spring.jpa.hibernate.ddl-auto=update
- news.api.key=YOUR_NEWSAPI_KEY
- spring.redis.host=localhost
- spring.redis.port=6379

Sensitive values can be set via environment variables (e.g., NEWS_API_KEY).

## Running with Docker

1. Build the image:
   - docker build -t news-aggregator .
2. Run (example with env vars):
   - docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://... -e NEWS_API_KEY=... -p 8080:8080 news-aggregator

If a docker-compose.yml is provided, use docker-compose up --build.

## Scheduling & Fetching

- Fetch jobs can be scheduled using Spring's @Scheduled on service methods.
- The project includes a NewsService that calls external NewsAPI and persists articles via Spring Data JPA. Configure rate-limiting and caching (Redis) to avoid hitting upstream limits.

## Testing

- Unit tests: ./mvnw test or ./gradlew test
- Integration tests: configure a test database (e.g., testcontainers) and run integration suite
- Linting / static analysis: run your chosen tools (SpotBugs, Checkstyle, etc.)

## Adding a new source

1. Implement a source adapter/service that:
   - Retrieves raw content (RSS/API/scrape)
   - Maps fields to the canonical Article entity (title, summary, content, url, publishedAt, source)
   - Persists via repository and includes tests
2. Register the adapter in scheduled fetch pipeline or as a REST endpoint.

## Observability

- Use structured logs (Spring Boot logging configuration)
- Expose metrics (Micrometer / Prometheus) for request rate, error rate, fetch durations, cache hit rate
- Consider tracing (OpenTelemetry) for fetch pipelines

## Troubleshooting

- Check application logs for errors
- Verify API keys and upstream availability
- Verify DB connectivity and migrations
- Re-run a single source fetch in debug to inspect raw payloads

## Contributing

- Fork, branch, open a PR
- Run tests locally and follow code style
- Add tests for new adapters or behavior changes
- Document architecture or API changes

## License

Add your project license (e.g., MIT) and include a LICENSE file in repo root.

(End of README)

- Tracing (optional) for fetch pipelines

## Deployment notes

- Use environment-specific configuration
- Run migrations before deploy
- Use a process manager (systemd, PM2) or container orchestration (Kubernetes)
- Ensure secrets are stored securely (vault/secret manager)
- Use health checks and readiness probes

## Troubleshooting

- Check logs for failed fetch jobs and parsing errors
- Verify API keys and endpoint availability
- Inspect cache/DB for stuck or duplicate entries
- Re-run fetches for a single source in debug mode to observe raw payloads

## Security & Compliance

- Store secrets out of repo
- Respect source terms of service and copyright laws
- Rate-limit and cache to avoid abusive traffic patterns

## Contributing

- Fork, branch, and open pull requests
- Follow repository coding style and run tests locally
- Add tests for new adapters or behavior changes
- Document significant architecture or API changes

## License

Specify your project license (e.g., MIT). Add LICENSE file in repo root.

## Contact / Further notes

- Add maintainer contact details and links to issue tracker
- Add roadmap or TODO for major features

(End of README)
