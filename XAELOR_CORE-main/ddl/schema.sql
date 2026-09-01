-- =========================================================
-- DDL - Projeto XAELOR_CORE
-- Banco: MySQL (Containerizado)
-- Gerado com base nas entidades JPA do projeto
-- =========================================================

CREATE DATABASE IF NOT EXISTS db_xaelor;
USE db_xaelor;

-- =========================================================
-- Tabela: TB_PERFUME
-- =========================================================
CREATE TABLE TB_PERFUME (
    PERFUME_ID          BIGINT AUTO_INCREMENT PRIMARY KEY,
    PERFUME_NOME        VARCHAR(255) NOT NULL,
    PERFUME_GENERO      VARCHAR(255) NOT NULL,
    PERFUME_DESCRICAO   VARCHAR(255)
);

-- =========================================================
-- Tabela: TB_MATERIAPRIMA
-- Coluna MATPRIMA_TIPOUNIDADE aceita: ML, L, MG, G, GOTA, unidade
-- =========================================================
CREATE TABLE TB_MATERIAPRIMA (
    MATPRIMA_ID             BIGINT AUTO_INCREMENT PRIMARY KEY,
    MATPRIMA_NOME           VARCHAR(255) NOT NULL,
    MATPRIMA_TIPOUNIDADE    VARCHAR(50) NOT NULL,
    MATPRIMA_DESCRICAO      VARCHAR(255)
);

-- =========================================================
-- Tabela: TB_PERFUME_MATERIA_PRIMA (associativa)
-- Coluna PERMAT_TIPOUNIDADE aceita: ML, L, MG, G, GOTA, unidade
-- =========================================================
CREATE TABLE TB_PERFUME_MATERIA_PRIMA (
    PERMAT_ID               BIGINT AUTO_INCREMENT PRIMARY KEY,
    PERFUME_ID              BIGINT NOT NULL,
    PERFUME_NOME            BIGINT NOT NULL,
    MATPRIMA_ID             BIGINT NOT NULL,
    MATPRIMA_NOME           BIGINT NOT NULL,
    PERMAT_VALORUNIDADE     DOUBLE NOT NULL,
    PERMAT_TIPOUNIDADE      VARCHAR(50) NOT NULL,
    PERMAT_CUSTOTOTAL       DOUBLE NOT NULL,
    PERMAT_MARGEMLUCRO      DECIMAL(38,2) NOT NULL,
    PERMAT_VALORFINAL       DOUBLE NOT NULL,

    CONSTRAINT fk_permat_perfume_id
        FOREIGN KEY (PERFUME_ID) REFERENCES TB_PERFUME (PERFUME_ID),

    CONSTRAINT fk_permat_perfume_nome
        FOREIGN KEY (PERFUME_NOME) REFERENCES TB_PERFUME (PERFUME_ID),

    CONSTRAINT fk_permat_matprima_id
        FOREIGN KEY (MATPRIMA_ID) REFERENCES TB_MATERIAPRIMA (MATPRIMA_ID),

    CONSTRAINT fk_permat_matprima_nome
        FOREIGN KEY (MATPRIMA_NOME) REFERENCES TB_MATERIAPRIMA (MATPRIMA_ID)
);