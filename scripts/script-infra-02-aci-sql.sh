#!/usr/bin/env bash
set -e

# SCRIPT 02 - SQL SERVER EM ACI

SUBSCRIPTION="2TDSPW-RM558024-CauãMarceloMachado"
RG_NAME="rg-healthhelp"
LOCATION="brazilsouth"

SQL_ACI_NAME="aci-healthhelp-sql"
DNS_SQL="healthhelp-sql-gs"   


if [ -z "$MSSQL_SA_PASSWORD" ]; then
  echo "ERRO: defina MSSQL_SA_PASSWORD antes de rodar. Ex:"
  echo "  export MSSQL_SA_PASSWORD=\"SqlAdmin#2025!\""
  exit 1
fi

echo ">> [SQL-ACI] Selecionando subscription..."
az account set --subscription "$SUBSCRIPTION"

echo ">> [SQL-ACI] Criando container ACI para SQL Server..."

az container create \
  --resource-group "$RG_NAME" \
  --name "$SQL_ACI_NAME" \
  --image "mcr.microsoft.com/mssql/server:2019-latest" \
  --os-type Linux \
  --cpu 2 \
  --memory 4 \
  --ports 1433 \
  --dns-name-label "$DNS_SQL" \
  --ip-address Public \
  --environment-variables \
    "ACCEPT_EULA=Y" \
    "MSSQL_SA_PASSWORD=$MSSQL_SA_PASSWORD"

echo ">> [SQL-ACI] Container criado."
echo "   Host: ${DNS_SQL}.${LOCATION}.azurecontainer.io"
echo "   Porta: 1433"
