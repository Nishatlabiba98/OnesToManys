DROP TABLE IF EXISTS building_plan;
DROP TABLE IF EXISTS builder;

CREATE TABLE builder (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    vision   VARCHAR(255) NOT NULL,
    location VARCHAR(150) NOT NULL
);

CREATE TABLE building_plan (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    builder_id  BIGINT       NOT NULL,
    title       VARCHAR(100) NOT NULL,
    style       VARCHAR(100) NOT NULL,
    square_feet INT          NOT NULL,
    notes       VARCHAR(500),
    CONSTRAINT fk_builder
        FOREIGN KEY (builder_id) REFERENCES builder(id)
        ON DELETE CASCADE
);