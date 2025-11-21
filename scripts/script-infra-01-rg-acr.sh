#!/usr/bin/env bash
set -e

# SCRIPT 01 - RG + ACR

SUBSCRIPTION="2TDSPW-RM558024-CauãMarceloMachado"
RG_NAME="rg-healthhelp"
LOCATION="brazilsouth"
ACR_NAME="acrhealthhelprm558024"

echo ">> [RG+ACR] Selecionando subscription..."
az account set --subscription "$SUBSCRIPTION"

echo ">> [RG+ACR] Criando/validando Resource Group: $RG_NAME"
az group create \
  --name "$RG_NAME" \
  --location "$LOCATION"

echo ">> [RG+ACR] Criando/validando Azure Container Registry: $ACR_NAME"
az acr create \
  --resource-group "$RG_NAME" \
  --name "$ACR_NAME" \
  --sku Basic \
  --admin-enabled true

echo ">> [RG+ACR] Testando acesso ao ACR..."
az acr show -n "$ACR_NAME" -o table

echo ">> [RG+ACR] Finalizado com sucesso."
