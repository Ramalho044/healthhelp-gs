/* ============================================================
   [01] CRIAÇÃO DO BANCO E SELEÇÃO
   ============================================================ */

IF DB_ID('HealthHelp') IS NULL
    CREATE DATABASE HealthHelp;
GO

USE HealthHelp;
GO

/* ============================================================
   [02] LIMPEZA COMPLETA DO ESQUEMA
   ============================================================ */

IF OBJECT_ID('dbo.trg_audit_usuario_insert', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_usuario_insert;
IF OBJECT_ID('dbo.trg_audit_usuario_update', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_usuario_update;
IF OBJECT_ID('dbo.trg_audit_usuario_delete', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_usuario_delete;

IF OBJECT_ID('dbo.trg_audit_registro_insert', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_registro_insert;
IF OBJECT_ID('dbo.trg_audit_registro_update', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_registro_update;
IF OBJECT_ID('dbo.trg_audit_registro_delete', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_registro_delete;

IF OBJECT_ID('dbo.trg_audit_atividade_insert', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_atividade_insert;
IF OBJECT_ID('dbo.trg_audit_atividade_update', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_atividade_update;
IF OBJECT_ID('dbo.trg_audit_atividade_delete', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_atividade_delete;

IF OBJECT_ID('dbo.trg_audit_recomendacao_insert', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_recomendacao_insert;
IF OBJECT_ID('dbo.trg_audit_recomendacao_update', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_recomendacao_update;
IF OBJECT_ID('dbo.trg_audit_recomendacao_delete', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_audit_recomendacao_delete;

IF OBJECT_ID('dbo.fn_validar_email', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_validar_email;
IF OBJECT_ID('dbo.fn_calc_score', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_calc_score;
IF OBJECT_ID('dbo.fn_gerar_json_rotina', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_gerar_json_rotina;

IF OBJECT_ID('dbo.prc_inserir_usuario', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_inserir_usuario;
IF OBJECT_ID('dbo.prc_export_json_usuario', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_export_json_usuario;
IF OBJECT_ID('dbo.prc_export_dataset_json', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_export_dataset_json;

IF OBJECT_ID('dbo.audit_log', 'U') IS NOT NULL DROP TABLE dbo.audit_log;
IF OBJECT_ID('dbo.recomendacao', 'U') IS NOT NULL DROP TABLE dbo.recomendacao;
IF OBJECT_ID('dbo.habito', 'U') IS NOT NULL DROP TABLE dbo.habito;
IF OBJECT_ID('dbo.atividade', 'U') IS NOT NULL DROP TABLE dbo.atividade;
IF OBJECT_ID('dbo.registro_diario', 'U') IS NOT NULL DROP TABLE dbo.registro_diario;
IF OBJECT_ID('dbo.categoria_atividade', 'U') IS NOT NULL DROP TABLE dbo.categoria_atividade;
IF OBJECT_ID('dbo.usuario', 'U') IS NOT NULL DROP TABLE dbo.usuario;
GO

/* ============================================================
   [03] TABELAS
   ============================================================ */

CREATE TABLE dbo.usuario (
    usuario_id     INT IDENTITY(1,1) PRIMARY KEY,
    nome           NVARCHAR(100) NOT NULL,
    email          NVARCHAR(200) NOT NULL UNIQUE,
    senha          NVARCHAR(200) NULL,
    genero         CHAR(1) NULL CHECK (genero IN ('M','F')),
    dt_nascimento  DATE NULL,
    altura_cm      DECIMAL(5,2) NULL,
    peso_kg        DECIMAL(6,2) NULL,
    dt_cadastro    DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME()
);
GO

CREATE TABLE dbo.categoria_atividade (
    categoria_id     INT IDENTITY(1,1) PRIMARY KEY,
    nome_categoria   NVARCHAR(60) NOT NULL UNIQUE
);
GO

CREATE TABLE dbo.registro_diario (
    registro_id          INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id           INT NOT NULL,
    data_ref             DATE NOT NULL,
    pontuacao_equilibrio DECIMAL(5,2) NULL,
    CONSTRAINT FK_registro_usuario FOREIGN KEY (usuario_id)
        REFERENCES dbo.usuario(usuario_id),
    CONSTRAINT UQ_registro_usuario_data UNIQUE (usuario_id, data_ref)
);
GO

CREATE TABLE dbo.atividade (
    atividade_id     INT IDENTITY(1,1) PRIMARY KEY,
    registro_id      INT NOT NULL,
    categoria_id     INT NOT NULL,
    descricao        NVARCHAR(200) NULL,
    inicio_ts        DATETIME2(0) NULL,
    fim_ts           DATETIME2(0) NULL,
    intensidade_1a5  TINYINT NULL,
    qualidade_1a5    TINYINT NULL,
    CONSTRAINT FK_atividade_registro FOREIGN KEY (registro_id)
        REFERENCES dbo.registro_diario(registro_id),
    CONSTRAINT FK_atividade_categoria FOREIGN KEY (categoria_id)
        REFERENCES dbo.categoria_atividade(categoria_id)
);
GO

CREATE TABLE dbo.habito (
    habito_id         INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id        INT NOT NULL,
    categoria_id      INT NOT NULL,
    nome              NVARCHAR(100) NULL,
    objetivo_min_dia  INT NULL,
    FOREIGN KEY (usuario_id) REFERENCES dbo.usuario(usuario_id),
    FOREIGN KEY (categoria_id) REFERENCES dbo.categoria_atividade(categoria_id)
);
GO

CREATE TABLE dbo.recomendacao (
    recomendacao_id   INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id        INT NOT NULL,
    data_ref          DATE NOT NULL,
    texto             NVARCHAR(1000) NULL,
    origem            NVARCHAR(50) NULL,
    score_relevancia  DECIMAL(5,2) NULL,
    FOREIGN KEY (usuario_id) REFERENCES dbo.usuario(usuario_id)
);
GO

CREATE TABLE dbo.audit_log (
    audit_id     INT IDENTITY(1,1) PRIMARY KEY,
    quando_ts    DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME(),
    tabela       NVARCHAR(40) NULL,
    operacao     NVARCHAR(10) NULL,
    chave        NVARCHAR(100) NULL,
    detalhes     NVARCHAR(4000) NULL
);
GO

/* ============================================================
   [04] TRIGGERS DE AUDITORIA
   ============================================================ */

CREATE TRIGGER dbo.trg_audit_usuario_insert ON dbo.usuario AFTER INSERT AS
INSERT INTO dbo.audit_log(tabela, operacao, chave, detalhes)
SELECT 'USUARIO','INSERT', i.usuario_id, 'email=' + i.email FROM inserted i;
GO

CREATE TRIGGER dbo.trg_audit_usuario_update ON dbo.usuario AFTER UPDATE AS
INSERT INTO dbo.audit_log(tabela, operacao, chave, detalhes)
SELECT 'USUARIO','UPDATE', i.usuario_id, 'email=' + i.email FROM inserted i;
GO

CREATE TRIGGER dbo.trg_audit_usuario_delete ON dbo.usuario AFTER DELETE AS
INSERT INTO dbo.audit_log(tabela, operacao, chave, detalhes)
SELECT 'USUARIO','DELETE', d.usuario_id, 'email=' + d.email FROM deleted d;
GO

/* ============================================================
   [05] FUNCTIONS
   ============================================================ */

CREATE FUNCTION dbo.fn_validar_email (@p_email NVARCHAR(200))
RETURNS INT AS
BEGIN
    RETURN CASE WHEN @p_email LIKE '%_@_%._%' THEN 1 ELSE 0 END;
END;
GO

CREATE FUNCTION dbo.fn_calc_score (@p_registro_id INT)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @v1 DECIMAL(10,2), @v2 DECIMAL(10,2);

    SELECT
        @v1 = ISNULL(AVG(CAST(intensidade_1a5 AS DECIMAL(10,2))), 0),
        @v2 = ISNULL(AVG(CAST(qualidade_1a5  AS DECIMAL(10,2))), 0)
    FROM dbo.atividade
    WHERE registro_id = @p_registro_id;

    RETURN ROUND((@v1 * 6 + @v2 * 4) * 2, 2);
END;
GO

/* ============================================================
   [06] PROCEDURES
   ============================================================ */

CREATE PROCEDURE dbo.prc_inserir_usuario
(
    @p_nome NVARCHAR(100),
    @p_email NVARCHAR(200),
    @p_senha NVARCHAR(200),
    @p_dt_nasc DATE = NULL,
    @p_altura DECIMAL(5,2) = NULL,
    @p_peso DECIMAL(6,2) = NULL,
    @p_genero CHAR(1) = NULL,
    @p_usuario_id INT OUTPUT
)
AS
BEGIN
    IF dbo.fn_validar_email(@p_email)=0
        THROW 50010, 'Email inválido', 1;

    INSERT INTO dbo.usuario(nome,email,senha,dt_nascimento,genero,altura_cm,peso_kg)
    VALUES (@p_nome,@p_email,@p_senha,@p_dt_nasc,@p_genero,@p_altura,@p_peso);

    SET @p_usuario_id=SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.prc_export_json_usuario
    @p_usuario_id INT,
    @p_json NVARCHAR(MAX) OUTPUT
AS
BEGIN
    SELECT @p_json = (
        SELECT
            u.usuario_id,
            u.nome,
            u.email,

            (SELECT r.data_ref AS data,
                    dbo.fn_calc_score(r.registro_id) AS score,
                    (SELECT a.categoria_id, a.descricao,
                            a.intensidade_1a5 AS intensidade,
                            a.qualidade_1a5 AS qualidade
                     FROM dbo.atividade a
                     WHERE a.registro_id = r.registro_id
                     FOR JSON PATH) AS atividades
             FROM dbo.registro_diario r
             WHERE r.usuario_id=u.usuario_id
             ORDER BY r.data_ref DESC
             FOR JSON PATH) AS rotina,

            (SELECT categoria_id, nome, objetivo_min_dia
             FROM dbo.habito WHERE usuario_id=u.usuario_id
             FOR JSON PATH) AS habitos,

            (SELECT data_ref AS data, texto, origem, score_relevancia
             FROM dbo.recomendacao WHERE usuario_id=u.usuario_id
             FOR JSON PATH) AS recomendacoes

        FROM dbo.usuario u
        WHERE u.usuario_id=@p_usuario_id
        FOR JSON PATH, WITHOUT_ARRAY_WRAPPER
    );
END;
GO

/* ============================================================
   [07] CARGA INICIAL
   ============================================================ */

-- Categorias
INSERT INTO dbo.categoria_atividade(nome_categoria)
VALUES
(N'SONO'),(N'TRABALHO'),(N'EXERCICIO'),(N'ALIMENTACAO'),(N'ESTUDO'),
(N'MEDITACAO'),(N'HIDRATACAO'),(N'LAZER'),(N'SOCIAL'),(N'MUSICA'),
(N'TRANSPORTE'),(N'RELAXAMENTO'),(N'PLANEJAMENTO'),(N'HOBBY'),(N'LEITURA');
GO

-- 30 usuários
;WITH nums AS (
    SELECT TOP 30 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS i
    FROM sys.all_objects
)
INSERT INTO dbo.usuario (nome,email,senha,dt_nascimento,genero,altura_cm,peso_kg)
SELECT
    CONCAT('Usuario ',i),
    CONCAT('usuario',RIGHT('00'+CAST(i AS VARCHAR(2)),2),'@healthhelp.com'),
    '123456',
    DATEADD(MONTH,-(i*10),'1990-01-01'),
    CASE WHEN i%2=0 THEN 'F' ELSE 'M' END,
    160+(i%15),
    60+(i%20)
FROM nums;
GO

-- 15 registros por usuário
;WITH u AS (SELECT usuario_id FROM dbo.usuario),
d AS (SELECT TOP 15 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS d FROM sys.all_objects)
INSERT INTO dbo.registro_diario(usuario_id,data_ref,pontuacao_equilibrio)
SELECT
    u.usuario_id,
    DATEADD(DAY,-d.d,CAST(SYSUTCDATETIME() AS DATE)),
    CAST((40+(RAND(CHECKSUM(NEWID()))*50.0)) AS DECIMAL(5,2))
FROM u CROSS JOIN d;
GO

-- Atividades
;WITH r AS (SELECT registro_id, data_ref FROM dbo.registro_diario),
c AS (SELECT categoria_id FROM dbo.categoria_atividade WHERE categoria_id<=5)
INSERT INTO dbo.atividade(registro_id,categoria_id,descricao,inicio_ts,fim_ts,intensidade_1a5,qualidade_1a5)
SELECT
    r.registro_id,
    c.categoria_id,
    N'Atividade rotineira',
    DATEADD(HOUR,c.categoria_id+7,CAST(r.data_ref AS DATETIME2(0))),
    DATEADD(HOUR,c.categoria_id+8,CAST(r.data_ref AS DATETIME2(0))),
    CAST(1+(ABS(CHECKSUM(NEWID()))%5) AS TINYINT),
    CAST(1+(ABS(CHECKSUM(NEWID()))%5) AS TINYINT)
FROM r CROSS JOIN c;
GO

-- Hábitos
INSERT INTO dbo.habito(usuario_id,categoria_id,nome,objetivo_min_dia)
SELECT usuario_id,3,N'Exercicio Diário',30 FROM dbo.usuario;
GO

-- Recomendações
INSERT INTO dbo.recomendacao(usuario_id,data_ref,texto,origem,score_relevancia)
SELECT
    usuario_id,
    CAST(SYSUTCDATETIME() AS DATE),
    N'Sugestão gerada automaticamente',
    N'AI',
    CAST(70+(RAND(CHECKSUM(NEWID()))*30.0) AS DECIMAL(5,2))
FROM dbo.usuario;
GO
