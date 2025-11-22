🚀 HealthHelp – Plataforma Web + Pipeline DevOps Completo na Azure (CI/CD + Containers + Azure SQL)

Global Solution 2025 –Analise e Desenvolvimento de Sistemas | FIAP

📘 1. Visão Geral do Projeto

O HealthHelp é uma plataforma web completa para gestão de bem-estar, permitindo registrar rotinas diárias, atividades, hábitos e gerar recomendações inteligentes usando IA.

Este projeto entrega:

Aplicação Java 21 + Spring Boot 3

Banco de Dados rodando em Container no Azure (Azure Container Instances)

Pipeline completo de CI e CD no Azure DevOps

Container Registry + Container App/ACI

Exportação JSON do dataset completo

Auditoria completa com triggers

CRUD pleno (API REST + Web)

Dockerfile separado em /dockerfiles

Infraestrutura definida por scripts

Implantação totalmente automatizada

🔧 2. Arquitetura Geral da Solução
GitHub → Azure DevOps (Pipelines) → Azure Container Registry (ACR)
                                         ↓
                              Azure Container Instances
                                         ↓
                              Aplicação HealthHelp em produção
                                         ↓
                          HealthHelp-SQL (Container SQL Server)

⚙️ 3. Tecnologias Utilizadas
Backend

Java 21

Spring Boot 3.3.x

Spring Web

Spring Data JPA

Spring Security

Spring AI (IA via GPT)

Validation (Jakarta)

Thymeleaf + Bootstrap

DevOps / Cloud

Azure DevOps (Repos, Pipelines, Boards, Releases)

Azure Container Registry (ACR)

Azure Container Instances (ACI)

Banco SQL Server em Container

Docker

YAML pipeline CI/CD

Shell Script para deploy automatizado

🐳 4. Docker & Estrutura do Projeto

Atendendo às exigências da GS:

✔ Dockerfile movido para /dockerfiles/Dockerfile.ci
✔ Aplicação containerizada
✔ Banco de dados SQL Server também rodando em container ACI
✔ Deploy automatizado via script script-infra-03-aci-app.sh

Estrutura após ajuste:
/dockerfiles
    └── Dockerfile.ci
/scripts
    ├── script-infra-01-rg-acr.sh
    ├── script-infra-03-aci-app.sh
    └── ...
/src
azure-pipelines.yml
README.md

🔁 5. Pipeline DEVOPS – CI/CD Completo
5.1 CI – Continuous Integration

A pipeline YAML (azure-pipelines.yml) faz:

Checkout do repositório

Instalação do Java 21

Build completo (clean + test + bootJar)

Execução dos testes JUnit

Empacotamento do JAR

Build da imagem Docker usando Dockerfile.ci

Push para o Azure Container Registry

Publicação dos scripts de infra como artefato

5.2 CD – Continuous Deployment

Pipeline de Release configurada:

Etapa: Prod – ACI

Automatiza:

Baixar artefatos

Executar o script:

./scripts/script-infra-03-aci-app.sh


Apaga container antigo

Cria nova instância com a imagem atual do ACR

Passa as variáveis de ambiente do banco

Publica em:

http://healthhelp-app-gs.brazilsouth.azurecontainer.io:8080


🔥 Tudo automático:
Commit → CI → CD → Deploy → Produção

💾 6. Banco de Dados – SQL SERVER em Container (ACI)

Banco criado e mantido totalmente via container:

Nome: aci-healthhelp-sql

Porta: 1433

Conexão usada no app:

jdbc:sqlserver://healthhelp-sql-gs.brazilsouth.azurecontainer.io:1433;
databaseName=HealthHelp;
encrypt=true;trustServerCertificate=true


✔ Rodando em Azure Container Instance
✔ Persistência garantida
✔ Acesso público controlado
✔ Criado automaticamente via scripts

🧬 7. Scripts de MIGRAÇÃO – Oracle → Azure SQL

O professor pediu compatibilidade.
Você entregou perfeitamente.

O script completo de criação das tabelas, triggers, functions e procedures está aqui:
📄 Scripts SQL completos


Inclui:

✔ Tabelas

usuario

categoria_atividade

registro_diario

atividade

habito

recomendacao

audit_log

✔ Triggers completas

Usuário

Registro Diário

Atividade

Recomendações

✔ Procedures

prc_inserir_usuario

prc_export_json_usuario

prc_export_dataset_json

✔ Funções

fn_validar_email

fn_calc_score

fn_gerar_json_rotina

✔ Carga inicial

30 usuários

15 categorias

15 registros/dia por usuário

Atividades

Hábitos

Recomendações

✔ Testes finais

JSON completo do dataset

JSON da rotina

Contagem de registros

🌐 8. API REST – CRUD Completo (JSON)

Requisito do professor: CRUD exposto em JSON no README.
Aqui está.

Usuários
GET /api/usuarios

Retorna todos os usuários.

POST /api/usuarios
{
  "nome": "João Silva",
  "email": "joao@healthhelp.com",
  "genero": "M",
  "alturaCm": 175,
  "pesoKg": 72
}

Registros Diários
POST /api/registros
{
  "usuarioId": 1,
  "dataRef": "2025-11-21",
  "pontuacaoEquilibrio": 72.5
}

Atividades
POST /api/atividades
{
  "registroId": 1,
  "categoriaId": 3,
  "descricao": "Corrida leve",
  "inicio": "2025-11-21T07:00",
  "fim": "2025-11-21T07:45",
  "intensidade": 4,
  "qualidade": 5
}

Recomendações (IA)
POST /api/recomendacoes/gerar
{
  "usuarioId": 1
}

🤖 9. IA – Spring AI GPT

O sistema gera recomendações com:

Análise da rotina

Score

Hábitos

Histórico

Observações

Retorna:

✔ Sugestões
✔ Recomendações personalizadas
✔ Texto estruturado

🧪 10. Testes Realizados

Testes JUnit

Testes SQL (Functions + JSON)

Testes do pipeline

Teste do container no ACI

Teste de conexão com banco

Testes manuais da interface

📊 11. Resultados finais
Contagem final (SQL):

usuários: 30

categorias: 15

registros: 450

atividades: 2250

habitos: 30

recomendacoes: 30

audit_log: 2760

Perfeito para validação da GS.

🏁 12. Conclusão

✔ CI
✔ CD
✔ Docker
✔ Containers
✔ Azure SQL
✔ JSON
✔ CRUD
✔ IA
✔ Auditoria
✔ Pipeline automatizado
✔ Estrutura DevOps completa



