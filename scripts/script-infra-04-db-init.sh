#!/usr/bin/env bash
set -e

# SCRIPT 04 - INICIALIZAR BANCO HEALTHHELP NO SQL EM ACI

SUBSCRIPTION="2TDSPW-RM558024-CauãMarceloMachado"
RG_NAME="rg-healthhelp"
SQL_ACI_NAME="aci-healthhelp-sql"

# Senha correta do SA (MESMA usada na criação do container)
SA_PASSWORD="${MSSQL_SA_PASSWORD:-SqlAdmin!2025!}"

echo ">> [DB-INIT] Selecionando subscription..."
az account set --subscription "$SUBSCRIPTION"

echo ">> [DB-INIT] Buscando FQDN do container SQL..."
SQL_FQDN=$(az container show \
  --resource-group "$RG_NAME" \
  --name "$SQL_ACI_NAME" \
  --query "ipAddress.fqdn" -o tsv)

if [ -z "$SQL_FQDN" ]; then
  echo "ERRO: não encontrei o container $SQL_ACI_NAME em $RG_NAME"
  exit 1
fi

echo ">> [DB-INIT] Usando servidor: $SQL_FQDN"

SCRIPT_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/script-bd.sql"

if [ ! -f "$SCRIPT_PATH" ]; then
  echo "ERRO: script-bd.sql não encontrado em $SCRIPT_PATH"
  exit 1
fi

echo ">> [DB-INIT] Aplicando script do banco..."
/opt/mssql-tools18/bin/sqlcmd \
  -S "${SQL_FQDN},1433" \
  -U sa \
  -P "$SA_PASSWORD" \
  -d master \
  -i "$SCRIPT_PATH" \
  -C

echo ">> [DB-INIT] Banco HealthHelp criado e populado com sucesso!"
