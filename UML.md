# OnesToManys — UML Diagrams

This file contains UML diagrams (written in [Mermaid](https://mermaid.js.org/)) that describe the architecture and design of the **OnesToManys (ListDetails)** 3-tier web application.

---

## 1. System Architecture — 3-Tier Component Diagram

Shows how the three tiers (Frontend, REST API, Database) relate to each other.

```mermaid
graph TD
    subgraph "Tier 1 — Presentation / Frontend"
        VJS[Vanilla JS App]
        React[React App]
    end

    subgraph "Tier 2 — Business Logic / REST API"
        Controller[Controller / Routes]
        Service[Service Layer]
        Repository[Repository / DAO]
        DTOs[DTOs\n ListDTO · DetailDTO]
    end

    subgraph "Tier 3 — Data / Database"
        DB[(Relational Database\nSQLite / PostgreSQL)]
    end

    VJS  -->|HTTP JSON| Controller
    React -->|HTTP JSON| Controller
    Controller --> Service
    Service --> Repository
    Service --> DTOs
    Repository -->|SQL Queries| DB
    DB -->|Result Sets| Repository
    Repository --> DTOs
    DTOs --> Controller
```

---

## 2. Entity-Relationship (ER) Diagram

Database schema showing the one-to-many relationship between the **Master** table and the **Detail** table.

```mermaid
erDiagram
    MASTER ||--o{ DETAIL : "has many"

    MASTER {
        int  id          PK
        string name
        string description
        date   created_at
    }

    DETAIL {
        int  id          PK
        int  master_id   FK
        string title
        string content
        date   created_at
    }
```

> Replace `MASTER` / `DETAIL` with your chosen relation (e.g. **Course** / **Student**, **Playlist** / **Song**, etc.).

---

## 3. Class Diagram

Shows the application layers: Entity classes, DTOs, Repository, Service, and Controller.

```mermaid
classDiagram
    direction TB

    %% Entity classes (mapped to DB tables)
    class Master {
        +int id
        +String name
        +String description
        +Date createdAt
        +List~Detail~ details
    }

    class Detail {
        +int id
        +int masterId
        +String title
        +String content
        +Date createdAt
        +Master master
    }

    Master "1" --> "0..*" Detail : contains

    %% Data Transfer Objects
    class MasterListDTO {
        +int id
        +String name
        +String description
    }

    class MasterDetailDTO {
        +int id
        +String name
        +String description
        +Date createdAt
        +List~DetailListDTO~ details
    }

    class DetailListDTO {
        +int id
        +String title
    }

    class DetailDetailDTO {
        +int id
        +String title
        +String content
        +Date createdAt
        +int masterId
    }

    MasterDetailDTO *-- DetailListDTO

    %% Repository layer
    class MasterRepository {
        +findAll() List~Master~
        +findById(id) Master
        +save(master) Master
        +delete(id) void
    }

    class DetailRepository {
        +findAll() List~Detail~
        +findById(id) Detail
        +findByMasterId(masterId) List~Detail~
        +save(detail) Detail
        +delete(id) void
    }

    %% Service layer
    class MasterService {
        +getAll() List~MasterListDTO~
        +getById(id) MasterDetailDTO
        +create(dto) MasterDetailDTO
        +update(id, dto) MasterDetailDTO
        +delete(id) void
        +getDetails(masterId) List~DetailListDTO~
    }

    class DetailService {
        +getAll() List~DetailListDTO~
        +getById(id) DetailDetailDTO
        +create(dto) DetailDetailDTO
        +update(id, dto) DetailDetailDTO
        +delete(id) void
    }

    MasterService --> MasterRepository
    MasterService --> DetailRepository
    DetailService --> DetailRepository

    %% Controller layer
    class MasterController {
        +GET  /masters
        +GET  /masters/{id}
        +POST /masters
        +PUT  /masters/{id}
        +DELETE /masters/{id}
        +GET  /masters/{id}/details
    }

    class DetailController {
        +GET  /details
        +GET  /details/{id}
        +POST /details
        +PUT  /details/{id}
        +DELETE /details/{id}
    }

    MasterController --> MasterService
    DetailController --> DetailService
```

---

## 4. REST API Endpoint Map

Maps each URL pattern to its ListOf / DetailView role and HTTP method.

```mermaid
graph LR
    subgraph "URL Endpoints"
        E1["GET  /api/masters"]
        E2["GET  /api/masters/:id"]
        E3["POST /api/masters"]
        E4["PUT  /api/masters/:id"]
        E5["DELETE /api/masters/:id"]
        E6["GET  /api/masters/:id/details"]
        E7["GET  /api/details/:id"]
        E8["POST /api/details"]
        E9["PUT  /api/details/:id"]
        E10["DELETE /api/details/:id"]
    end

    subgraph "Pattern / Response"
        R1["ListOfMasters → MasterListDTO[]"]
        R2["DetailOfMaster → MasterDetailDTO"]
        R3["Create Master"]
        R4["Update Master"]
        R5["Delete Master"]
        R6["ListOfDetails → DetailListDTO[]"]
        R7["DetailOfDetail → DetailDetailDTO"]
        R8["Create Detail"]
        R9["Update Detail"]
        R10["Delete Detail"]
    end

    E1  --> R1
    E2  --> R2
    E3  --> R3
    E4  --> R4
    E5  --> R5
    E6  --> R6
    E7  --> R7
    E8  --> R8
    E9  --> R9
    E10 --> R10

    style E1 fill:#f9f,stroke:#333
    style R1 fill:#f9f,stroke:#333
    style E2 fill:#bbf,stroke:#333
    style R2 fill:#bbf,stroke:#333
    style E6 fill:#bfb,stroke:#333
    style R6 fill:#bfb,stroke:#333
    style E7 fill:#fbf,stroke:#333
    style R7 fill:#fbf,stroke:#333
```

---

## 5. Sequence Diagram — Full CRUD Flow

Shows the request/response lifecycle for each CRUD operation across all three tiers.

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repository
    participant DB as Database

    Note over Client,DB: READ — List all Masters
    Client->>Controller: GET /api/masters
    Controller->>Service: getAll()
    Service->>Repository: findAll()
    Repository->>DB: SELECT * FROM master
    DB-->>Repository: rows[]
    Repository-->>Service: List~Master~
    Service-->>Controller: List~MasterListDTO~
    Controller-->>Client: 200 JSON array

    Note over Client,DB: READ — Get one Master with its Details
    Client->>Controller: GET /api/masters/1
    Controller->>Service: getById(1)
    Service->>Repository: findById(1)
    Repository->>DB: SELECT … WHERE id=1
    DB-->>Repository: master row
    Service->>Repository: findByMasterId(1)
    Repository->>DB: SELECT … WHERE master_id=1
    DB-->>Repository: detail rows[]
    Repository-->>Service: List~Detail~
    Service-->>Controller: MasterDetailDTO
    Controller-->>Client: 200 JSON object

    Note over Client,DB: CREATE — New Master
    Client->>Controller: POST /api/masters {name, description}
    Controller->>Service: create(dto)
    Service->>Repository: save(master)
    Repository->>DB: INSERT INTO master …
    DB-->>Repository: new row (with id)
    Repository-->>Service: Master
    Service-->>Controller: MasterDetailDTO
    Controller-->>Client: 201 JSON object

    Note over Client,DB: UPDATE — Edit Master
    Client->>Controller: PUT /api/masters/1 {name, description}
    Controller->>Service: update(1, dto)
    Service->>Repository: save(updatedMaster)
    Repository->>DB: UPDATE master SET … WHERE id=1
    DB-->>Repository: updated row
    Repository-->>Service: Master
    Service-->>Controller: MasterDetailDTO
    Controller-->>Client: 200 JSON object

    Note over Client,DB: DELETE — Remove Master (cascades to Details)
    Client->>Controller: DELETE /api/masters/1
    Controller->>Service: delete(1)
    Service->>Repository: delete(1)
    Repository->>DB: DELETE FROM master WHERE id=1
    DB-->>Repository: ok (cascade deletes details)
    Repository-->>Service: void
    Service-->>Controller: void
    Controller-->>Client: 204 No Content
```

---

## 6. Use Case Diagram

Shows what a **User** can do with the application.

```mermaid
graph LR
    actor["👤 User"]

    subgraph "Master Management"
        UC1[View all Masters]
        UC2[View Master details]
        UC3[Create Master]
        UC4[Edit Master]
        UC5[Delete Master]
    end

    subgraph "Detail Management"
        UC6[View Details for a Master]
        UC7[View Detail record]
        UC8[Create Detail]
        UC9[Edit Detail]
        UC10[Delete Detail]
    end

    subgraph "Data Operations"
        UC11[Export data to SQL/JSON]
        UC12[Import/Load data]
    end

    actor --> UC1
    actor --> UC2
    actor --> UC3
    actor --> UC4
    actor --> UC5
    actor --> UC6
    actor --> UC7
    actor --> UC8
    actor --> UC9
    actor --> UC10
    actor --> UC11
    actor --> UC12
```

---

## 7. UI Navigation Flow

Shows how a user navigates through the web interface across the four view types.

```mermaid
stateDiagram-v2
    [*] --> MasterList : open app

    MasterList : 📋 List of Masters\n(ListOfMasters)
    MasterDetail : 📄 Master Detail\n(DetailOfMaster)
    DetailList : 📋 List of Details\n(ListOfDetails)
    DetailDetail : 📄 Detail Record\n(DetailOfDetail)

    MasterList --> MasterDetail : click a row
    MasterDetail --> MasterList : ← back
    MasterDetail --> DetailList : view details tab
    DetailList --> MasterDetail : ← back
    DetailList --> DetailDetail : click a row
    DetailDetail --> DetailList : ← back

    MasterList --> MasterList : create / edit / delete master
    DetailList --> DetailList : create / edit / delete detail
```

---

## 8. Phase Milestone Timeline

```mermaid
gantt
    title OnesToManys — Project Phases
    dateFormat  YYYY-MM-DD
    section Phase 1 (Days 1-2)
    Design DB schema          :p1a, 2024-01-01, 1d
    Write SQL schema file     :p1b, after p1a, 1d
    Generate seed data SQL    :p1c, after p1b, 1d
    Build REST CRUD endpoints :p1d, after p1c, 2d
    Test with curl            :p1e, after p1d, 1d
    section Phase 2 (Days 3-4)
    Add one-to-many endpoints :p2a, after p1e, 1d
    Test with Postman/Insomnia:p2b, after p2a, 1d
    Add dump/load feature     :p2c, after p2b, 1d
    section Phase 3 (Days 5-7)
    Vanilla JS frontend       :p3a, after p2c, 2d
    React frontend            :p3b, after p3a, 2d
    CRUD web pages            :p3c, after p3b, 1d
    One-to-many UI            :p3d, after p3c, 1d
```
