# Paper Trader

A beginner-friendly practice trading app. Enter a ticker and a number of
shares, and the app looks up the current price, records the trade, and shows
your practice portfolio and total spent. No real money is involved.

Author: Austin Lantz

## Technologies
- Java (JDK 21 or newer), Spring Boot, Thymeleaf
- Spring Data JPA with an H2 file database
- Maven (package/build manager)
- Finnhub market data API

## Setup
1. Install a JDK (21 or newer) and check with `java --version`.
2. Get a free API key at https://finnhub.io
3. Generate the Maven wrapper once (needs Maven installed), or generate a
   project at https://start.spring.io and copy `mvnw`, `mvnw.cmd` and `.mvn/`
   into this folder:
   ```
   mvn wrapper:wrapper
   ```
4. Run the app (Git Bash):
   ```
   export FINNHUB_API_KEY=your_key_here
   ./mvnw spring-boot:run
   ```
   On PowerShell: `$env:FINNHUB_API_KEY="your_key_here"; .\mvnw.cmd spring-boot:run`
5. Open http://localhost:8080

## Tests
```
./mvnw test
```

## Javadoc
```
./mvnw javadoc:javadoc
```

## What I learned
(Add your notes here: pros, cons, problems you hit, and what you would
recommend to your team.)
