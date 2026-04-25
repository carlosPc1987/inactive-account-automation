# inactive-account-automation

Inactive account lifecycle automation using a modular architecture based on Java 17 and Maven.

## Description

This repository provides the base for a backend system designed to manage inactive accounts in a structured and maintainable way.

The solution is organized into separate modules to support:

- separation of concerns
- scalability
- layer-based testing
- independent evolution by context

## Project architecture

The project uses a parent `pom.xml` with Maven modules:

- `bootstrap`: application entry point
- `domain-account`: domain model and core business rules
- `application-account`: use cases and application orchestration
- `infrastructure-account`: technical adapters (persistence, integrations, etc.)
- `shared-kernel`: shared components across modules

## Technical stack

- Java 17
- Maven (multi-module)

## Requirements

Before running the project, make sure you have:

- JDK 17 installed
- Maven 3.9+ installed
- Git installed

## Getting started

1. Clone the repository:

```bash
git clone https://github.com/carlosPc1987/inactive-account-automation.git
cd inactive-account-automation
```

2. Build all modules:

```bash
mvn clean install
```

3. Run tests:

```bash
mvn test
```

## Structure

```text
inactive-account-automation/
|- pom.xml
|- bootstrap/
|- domain-account/
|- application-account/
|- infrastructure-account/
`- shared-kernel/
```

## Recommended workflow

- create a branch from `main` for each feature or fix
- make small, descriptive commits
- open a Pull Request for review before merge

## Current status

Project initialized with a Maven multi-module base structure.

## Author

CarlosPC1987
