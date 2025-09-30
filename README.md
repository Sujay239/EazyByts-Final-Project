# News Aggregator

Short, focused README for the News Aggregator project. This document describes the project purpose, architecture, setup, configuration, how to run and test, and deployment notes.

## Overview

News Aggregator collects articles from multiple sources, normalizes content, stores results, and serves them via an API and/or web UI. It handles deduplication, caching, rate limits, and source-specific parsing.

## Key Features

- Fetch articles from multiple sources (RSS, APIs, scrapers)
- Normalize and deduplicate articles
- Persist articles in a database
- Serve via REST API (and optional UI)
- Cache results for performance and to respect source rate limits
- Extensible source adapters

## Architecture (high level)

- Fetcher(s): scheduled workers that pull from sources
- Normalizer: converts source-specific fields to a common model
- Storage: database for articles, metadata, and fetch history
- API: exposes search/list/detail endpoints
- Cache layer: in-memory or Redis to reduce load and rate-limit calls
- Optional UI: web frontend for browsing articles

## Prerequisites

- Git
- Node.js (>= 14) or Python (>= 3.8) depending on implementation, or Docker
- A database (Postgres/MySQL/SQLite for dev)
- Redis (optional, for caching)
- API keys for any paid news APIs you intend to use (optional)

## Quick start (local)

1. Clone repository
   - git clone <repo-url>
2. Install dependencies
   - Node: npm install
   - Python: pip install -r requirements.txt
3. Configure environment variables (see below)
4. Run database migrations (if applicable)
   - Example: npm run migrate OR alembic upgrade head
5. Start app
   - Node: npm start
   - Python: python -m app

## Quick start (Docker)

1. Build images:
   - docker build -t news-aggregator .
2. Run with docker-compose (if provided):
   - docker-compose up --build
3. Open the API at http://localhost:PORT

## Configuration / Environment variables

Common environment variables (adjust to your project):

- PORT=3000
- DATABASE_URL=postgres://user:pass@localhost:5432/newsdb
- REDIS_URL=redis://localhost:6379
- NEWS_API_KEY=your_key_here
- LOG_LEVEL=info
- FETCH_SCHEDULE=_/15 _ \* \* \* (cron for fetch jobs)

Place these in a .env file (do not commit secrets).

## Running tests

- Unit tests:
  - Node: npm test
  - Python: pytest
- Integration tests:
  - Configure test DB and run integration suite
- Linting:
  - npm run lint or flake8

## Adding a new source

1. Create a source adapter that:
   - Fetches raw content (RSS/API/scrape)
   - Maps raw fields to canonical model (title, summary, content, url, published_at, source_id)
   - Emits structured articles to normalizer/storage
2. Add scheduling entry (cron or queue job)
3. Add tests for parsing and edge cases
4. Document rate limits and legal/copyright considerations

## Caching & rate limits

- Use Redis or in-process cache for recently fetched responses.
- Respect upstream rate limits: backoff on 429, queue requests, and serialize heavy scrapes.
- Store fetch timestamps to avoid unnecessary requests.

## Data model (example)

- Article: id, title, content, summary, url, published_at, source, language, tags
- Source: id, name, type, endpoint, rate_limit
- FetchLog: source_id, fetched_at, status, item_count

## Observability

- Logging with structured logs (JSON)
- Metrics: request rate, error rate, fetch durations, cache hit rate
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
