# OnesToManys — Complete Study Guide
### Builder (master) → BuildingPlan (detail)

---

## Table of Contents
1. The Big Picture — How Everything Connects
2. H2 Database
3. API vs REST API
4. SQL Commands and How They Map to CRUD
5. File-by-File Breakdown
6. The HTML API Tester
7. How All the Files Connect to Each Other

---

## 1. The Big Picture

Your app is a **3-tier web application**:

```
TIER 1 — FRONTEND
index.html (Vanilla JS)
"What the user sees and clicks"
        ↕ fetch() calls over HTTP
TIER 2 — BACKEND
Spring Boot REST API
"The brain — handles requests, talks to DB"
        ↕ JPA/Hibernate SQL queries
TIER 3 — DATABASE
H2 In-Memory Database
"Where all the data lives"
```

When you click **View Plans** in the browser:
```
1. JavaScript runs: fetch('http://localhost:8080/api/builders/1/plans')
2. Spring Boot receives the request
3. BuilderController calls repo.findById(1)
4. JPA runs: SELECT * FROM building_plan WHERE builder_id = 1
5. H2 returns the rows
6. Spring converts them to JSON
7. JavaScript receives the JSON and renders the plan cards
```

---

## 2. H2 Database

**H2** is a lightweight Java database that runs inside your Spring Boot app.
You don't install it separately — it's just a dependency in your `pom.xml`.

### Two modes:
```
In-Memory (what you use):
spring.datasource.url=jdbc:h2:mem:listdetails
→ Data lives in RAM
→ Resets on every restart
→ Loads from data.sql on startup
→ No lock files, no file system

File-Based (not used):
spring.datasource.url=jdbc:h2:file:./data/listdetails
→ Data saved to disk
→ Survives restarts
→ Can cause lock issues
```

### H2 Console
Go to `http://localhost:8080/h2-console` to browse your database visually.
JDBC URL: `jdbc:h2:mem:listdetails`

### Why H2 for bootcamp?
- Zero setup — no MySQL/Postgres to install
- Runs inside the app
- Easy to reset (just restart)
- Good enough for learning

---

## 3. API vs REST API

### API (Application Programming Interface)
A way for two programs to talk to each other.

```
Think of it like a waiter:
You (browser/curl) → API (Spring Boot) → Kitchen (H2 database)
```

### REST API (Representational State Transfer)
A specific set of rules for how an API should work over the web.

REST uses:
- **URLs** to identify resources (what you want)
- **HTTP methods** to describe actions (what you want to do)
- **JSON** to send and receive data

### HTTP Methods — The 4 Actions (CRUD)

| HTTP Method | CRUD Action | SQL | Example |
|-------------|-------------|-----|---------|
| GET | Read | SELECT | Get all builders |
| POST | Create | INSERT | Add a new builder |
| PUT | Update | UPDATE | Edit a builder |
| DELETE | Delete | DELETE | Remove a builder |

### REST Rules — URLs should be nouns not verbs:
```
✅ REST way          ❌ Old way
GET /api/builders    /getBuilders
POST /api/builders   /createBuilder
PUT /api/builders/1  /updateBuilder?id=1
DELETE /api/builders/1 /deleteBuilder?id=1
```

---

## 4. SQL Commands and How They Map to CRUD

### Your schema.sql creates the tables:

```sql
CREATE TABLE builder (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,  -- unique ID, auto-increments
    name     VARCHAR(100) NOT NULL,               -- required text field
    vision   VARCHAR(255) NOT NULL,
    location VARCHAR(150) NOT NULL,
    icon     VARCHAR(10)                          -- optional emoji
);

CREATE TABLE building_plan (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    builder_id  BIGINT NOT NULL,                  -- foreign key
    title       VARCHAR(100) NOT NULL,
    style       VARCHAR(100) NOT NULL,
    square_feet INT NOT NULL,
    notes       VARCHAR(500),
    icon        VARCHAR(10),
    CONSTRAINT fk_builder
        FOREIGN KEY (builder_id) REFERENCES builder(id)
        ON DELETE CASCADE                         -- delete plans when builder deleted
);
```

### Your data.sql loads sample data:

```sql
INSERT INTO builder (name, vision, location, icon) VALUES
    ('Nishat', 'Cozy family spaces with heart', 'Salt Lake City, UT', '🏡');
```

### SQL ↔ CRUD ↔ HTTP Method mapping:

```
CREATE  →  POST   →  INSERT INTO builder (name, vision, location) VALUES (...)
READ    →  GET    →  SELECT * FROM builder WHERE id = 1
UPDATE  →  PUT    →  UPDATE builder SET name = ?, vision = ? WHERE id = 1
DELETE  →  DELETE →  DELETE FROM builder WHERE id = 1
```

### The one-to-many join:

```sql
-- Get all plans for builder #1 (Nishat)
SELECT bp.*
FROM building_plan bp
JOIN builder b ON bp.builder_id = b.id
WHERE b.id = 1;

-- Spring does this automatically when you call b.getPlans()
```

### H2 Console queries to try:

```sql
SELECT * FROM builder;
SELECT * FROM building_plan;
SELECT b.name, bp.title, bp.style, bp.square_feet
FROM builder b
JOIN building_plan bp ON b.id = bp.builder_id
WHERE b.id = 1;
```

---

## 5. File-by-File Breakdown

---

### `pom.xml` — The Project Recipe

```xml
<dependencies>
    <dependency>spring-boot-starter-web</dependency>     <!-- REST API -->
    <dependency>spring-boot-starter-data-jpa</dependency> <!-- Database ORM -->
    <dependency>h2</dependency>                          <!-- H2 database -->
</dependencies>
```

**What it does:** Tells Maven which libraries to download.
Think of it like a `package.json` in Node.js.

---

### `application.properties` — The Config File

```properties
spring.datasource.url=jdbc:h2:mem:listdetails  ← connect to H2 in memory
spring.jpa.hibernate.ddl-auto=none             ← don't auto-create tables (use schema.sql)
spring.sql.init.mode=always                    ← always run schema.sql and data.sql
spring.h2.console.enabled=true                 ← enable browser DB console
server.port=8080                               ← run on port 8080
```

**What it does:** Configures how Spring Boot behaves. No Java code here — just settings.

---

### `schema.sql` — The Database Blueprint

Creates your two tables from scratch every time the app starts.
The `DROP TABLE IF EXISTS` at the top ensures a clean slate on every restart.

**Connects to:** H2 database directly. Runs before `data.sql`.

---

### `data.sql` — The Sample Data

Inserts your 4 builders (Nishat, Josh, Alani, Nishan) and 8 building plans.
Runs after `schema.sql` on every startup.

**Connects to:** H2 database. Depends on `schema.sql` having run first.

---

### `ListdetailsApplication.java` — The Entry Point

```java
@SpringBootApplication
public class ListdetailsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListdetailsApplication.class, args);
    }
}
```

**What it does:** The `main()` method — this is where Java starts.
`@SpringBootApplication` tells Spring to scan all packages and wire everything up.

---

### `Builder.java` — The Master Entity

```java
@Entity           ← tells JPA this class maps to a database table
@Table(name = "builder")  ← maps to the "builder" table in H2
public class Builder {

    @Id                              ← this is the primary key
    @GeneratedValue(...)             ← auto-increment the id
    private Long id;

    private String name;             ← maps to "name" column
    private String vision;           ← maps to "vision" column
    private String location;         ← maps to "location" column
    private String icon;             ← maps to "icon" column

    @OneToMany(mappedBy = "builder", cascade = CascadeType.ALL)
    private List<BuildingPlan> plans; ← one builder has many plans
}
```

**What it does:** Represents one row in the `builder` table as a Java object.
`@OneToMany` tells JPA to load all plans that belong to this builder.
`cascade = CascadeType.ALL` means when you delete a builder, all their plans get deleted too.

---

### `BuildingPlan.java` — The Detail Entity

```java
@Entity
@Table(name = "building_plan")
public class BuildingPlan {

    @Id
    @GeneratedValue(...)
    private Long id;

    private String title;
    private String style;
    private int squareFeet;
    private String notes;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "builder_id")                    ← creates the FK column
    @JsonIgnoreProperties({"plans", "hibernateLazyInitializer"})
    private Builder builder;                            ← many plans belong to one builder
}
```

**What it does:** Represents one row in the `building_plan` table.
`@ManyToOne` is the other side of the relationship — many plans belong to one builder.
`@JoinColumn(name = "builder_id")` creates the foreign key column.
`@JsonIgnoreProperties` prevents infinite loops when converting to JSON
(Builder → plans → Builder → plans → ...).

---

### `BuilderRepository.java` — The Database Gateway

```java
public interface BuilderRepository extends JpaRepository<Builder, Long> {}
```

**What it does:** Spring Data JPA generates ALL the SQL automatically just from this interface.
You get these methods for free without writing any SQL:
```
repo.findAll()         → SELECT * FROM builder
repo.findById(1L)      → SELECT * FROM builder WHERE id = 1
repo.save(builder)     → INSERT or UPDATE
repo.deleteById(1L)    → DELETE FROM builder WHERE id = 1
repo.existsById(1L)    → SELECT COUNT(*) FROM builder WHERE id = 1
```

---

### `BuildingPlanRepository.java` — The Plan Gateway

```java
public interface BuildingPlanRepository extends JpaRepository<BuildingPlan, Long> {
    List<BuildingPlan> findByBuilderId(Long builderId);
}
```

**What it does:** Same as BuilderRepository but for plans.
The extra method `findByBuilderId` tells Spring to generate:
```sql
SELECT * FROM building_plan WHERE builder_id = ?
```
Just from the method name — no SQL written!

---

### `BuilderController.java` — The REST API for Builders

This is where HTTP requests come in and get handled.

```java
@RestController              ← this class handles HTTP requests and returns JSON
@RequestMapping("/api/builders")  ← all methods start with /api/builders

@GetMapping                  → GET /api/builders         → SELECT * FROM builder
@GetMapping("/{id}")         → GET /api/builders/1       → SELECT WHERE id = 1
@GetMapping("/{id}/plans")   → GET /api/builders/1/plans → get Nishat's plans
@GetMapping("/export")       → GET /api/builders/export  → dump all data
@PostMapping                 → POST /api/builders        → INSERT new builder
@PutMapping("/{id}")         → PUT /api/builders/1       → UPDATE builder
@DeleteMapping("/{id}")      → DELETE /api/builders/1    → DELETE builder
```

Key annotations:
```java
@PathVariable  ← pulls {id} out of the URL
@RequestBody   ← reads JSON from the request body into a Java object
ResponseEntity ← wraps the response with an HTTP status code (200, 404, etc.)
```

---

### `BuildingPlanController.java` — The REST API for Plans

Same pattern as BuilderController but for plans.

```java
@RestController
@RequestMapping("/api/plans")

@GetMapping          → GET /api/plans        → all plans
@GetMapping("/{id}") → GET /api/plans/1      → one plan
@PostMapping         → POST /api/plans       → create plan (needs builder.id in body)
@PutMapping("/{id}") → PUT /api/plans/1      → update plan
@DeleteMapping       → DELETE /api/plans/1   → delete plan
```

Key difference from BuilderController: the `create()` method looks up the builder
from the database before saving, ensuring the foreign key is set correctly.

---

### `index.html` — The Frontend (Phase 3)

A single HTML file with embedded CSS and JavaScript.

**Structure:**
```
HTML  → defines the page structure (cards, modals, buttons)
CSS   → styles everything (dark barn theme, animations, layout)
JS    → talks to your Spring API using fetch()
```

**Key JavaScript functions:**
```javascript
loadBuilders()       → GET /api/builders → renders builder cards
selectBuilder(id)    → GET /api/builders/{id}/plans → renders plan cards
saveBuilder()        → POST or PUT /api/builders → create or update
deleteBuilder(id)    → DELETE /api/builders/{id}
savePlan()           → POST or PUT /api/plans → create or update
deletePlan(id)       → DELETE /api/plans/{id}
```

**How fetch() works:**
```javascript
fetch('http://localhost:8080/api/builders')   // sends GET request
  .then(res => res.json())                    // parses JSON response
  .then(builders => renderBuilders(builders)) // updates the UI
```

---

### `api-tester.html` — The GUI REST Client (Phase 2)

A built-in REST client that lets you test all your endpoints from the browser.
Lives at `http://localhost:8080/api-tester.html`.

**What it does:**
- Lists all your endpoints in the sidebar
- Pre-fills request bodies for POST/PUT
- Shows the curl command for every request
- Displays the JSON response with timing

---

## 6. How All the Files Connect to Each Other

```
pom.xml
└── downloads all Spring Boot libraries

application.properties
├── connects to H2 database
├── tells Spring to run schema.sql + data.sql
└── enables H2 console

schema.sql → creates tables in H2
data.sql   → fills tables with Nishat/Josh/Alani/Nishan data

Builder.java ←→ building_plan table (via @Entity)
BuildingPlan.java ←→ building_plan table (via @Entity)
    └── Builder.java via @ManyToOne (builder_id FK)

BuilderRepository.java
    └── uses Builder.java to talk to builder table

BuildingPlanRepository.java
    └── uses BuildingPlan.java to talk to building_plan table

BuilderController.java
    ├── uses BuilderRepository to read/write builders
    └── exposes /api/builders endpoints

BuildingPlanController.java
    ├── uses BuildingPlanRepository to read/write plans
    ├── uses BuilderRepository to look up builder on create
    └── exposes /api/plans endpoints

index.html
    └── calls BuilderController + BuildingPlanController via fetch()

api-tester.html
    └── calls all endpoints via fetch() for testing
```

---

## 7. The Request Journey (End to End)

When you click **+ Add Plan** and save:

```
1. index.html (JS)
   savePlan() builds JSON:
   { title, style, squareFeet, notes, icon, builder: { id: 1 } }

2. fetch() sends POST to http://localhost:8080/api/plans

3. BuildingPlanController.create() receives the request
   @RequestBody converts JSON → BuildingPlan Java object

4. Controller looks up builder:
   builderRepo.findById(1) → SELECT * FROM builder WHERE id = 1

5. Sets builder on plan:
   plan.setBuilder(builder)

6. Saves to database:
   repo.save(plan) → INSERT INTO building_plan (...) VALUES (...)

7. H2 stores the row and returns it with a new id

8. Spring converts BuildingPlan → JSON and sends it back

9. index.html calls selectBuilder() to refresh the plan grid

10. You see the new plan card appear on screen
```

---

## Quick Reference — All Endpoints

```
GET    /api/builders              → all builders
GET    /api/builders/{id}         → one builder
GET    /api/builders/{id}/plans   → one builder's plans ⭐ (nested)
GET    /api/builders/export       → dump all data as JSON
POST   /api/builders              → create builder
PUT    /api/builders/{id}         → update builder
DELETE /api/builders/{id}         → delete builder

GET    /api/plans                 → all plans
GET    /api/plans/{id}            → one plan
POST   /api/plans                 → create plan
PUT    /api/plans/{id}            → update plan
DELETE /api/plans/{id}            → delete plan
```

## Quick Reference — Annotations

```
@SpringBootApplication   → marks the main class, starts everything
@Entity                  → this class maps to a database table
@Table(name="...")       → specifies which table
@Id                      → marks the primary key
@GeneratedValue          → auto-increment the id
@OneToMany               → one builder has many plans
@ManyToOne               → many plans belong to one builder
@JoinColumn              → creates the foreign key column
@JsonIgnoreProperties    → prevents infinite JSON loops
@RestController          → this class handles HTTP requests
@RequestMapping          → sets the base URL path
@GetMapping              → handles GET requests
@PostMapping             → handles POST requests
@PutMapping              → handles PUT requests
@DeleteMapping           → handles DELETE requests
@PathVariable            → pulls {id} from the URL
@RequestBody             → reads JSON from request body
```
