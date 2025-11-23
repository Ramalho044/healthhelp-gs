/* ============================================================
   [01] LIMPEZA COMPLETA DO ESQUEMA
   Remove tudo antes de recriar (tabelas, triggers, functions etc.)
   ============================================================ */

-- DROP TRIGGERS
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

-- DROP FUNCTIONS & PROCEDURES
IF OBJECT_ID('dbo.fn_validar_email', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_validar_email;
IF OBJECT_ID('dbo.fn_calc_score', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_calc_score;
IF OBJECT_ID('dbo.fn_gerar_json_rotina', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_gerar_json_rotina;
IF OBJECT_ID('dbo.prc_inserir_usuario', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_inserir_usuario;
IF OBJECT_ID('dbo.prc_export_json_usuario', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_export_json_usuario;
IF OBJECT_ID('dbo.prc_export_dataset_json', 'P') IS NOT NULL DROP PROCEDURE dbo.prc_export_dataset_json;

-- DROP TABLES
IF OBJECT_ID('dbo.audit_log', 'U') IS NOT NULL DROP TABLE dbo.audit_log;
IF OBJECT_ID('dbo.recomendacao', 'U') IS NOT NULL DROP TABLE dbo.recomendacao;
IF OBJECT_ID('dbo.habito', 'U') IS NOT NULL DROP TABLE dbo.habito;
IF OBJECT_ID('dbo.atividade', 'U') IS NOT NULL DROP TABLE dbo.atividade;
IF OBJECT_ID('dbo.registro_diario', 'U') IS NOT NULL DROP TABLE dbo.registro_diario;
IF OBJECT_ID('dbo.categoria_atividade', 'U') IS NOT NULL DROP TABLE dbo.categoria_atividade;
IF OBJECT_ID('dbo.usuario', 'U') IS NOT NULL DROP TABLE dbo.usuario;
GO



/* ============================================================
   [02] CRIAÇÃO DAS TABELAS
   ============================================================ */

CREATE TABLE dbo.usuario (
    usuario_id     INT IDENTITY(1,1) PRIMARY KEY,
    nome           NVARCHAR(100) NOT NULL,
    email          NVARCHAR(200) NOT NULL UNIQUE,
    senha          NVARCHAR(60) NULL,              -- NOVA COLUNA SENHA
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
    CONSTRAINT FK_registro_usuario
        FOREIGN KEY (usuario_id) REFERENCES dbo.usuario(usuario_id),
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
    intensidade_1a5  TINYI_
