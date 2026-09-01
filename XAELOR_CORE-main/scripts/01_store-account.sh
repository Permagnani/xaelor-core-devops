#!/bin/bash

# =========================================================
# 01_store-account.sh
# Cria o Resource Group (se nao existir) e a Conta de
# Armazenamento com o File Share usado como volume do MySQL
# =========================================================

# Variaveis
rm=564995
#
storageAccountName="stgxaelor$rm"
#
file_share_name="mysql-xaelor-volume"
resourceGroup="rg-xaelor-core"
location="canadacentral"

# Valida se o Grupo de Recursos existe e cria caso nao exista
if ! az group show --name "$resourceGroup" &>/dev/null; then
  echo "Resource group '$resourceGroup' nao existe. Criando..."
  az group create --name "$resourceGroup" --location "$location"
fi

# Registra o Servico de Storage na Assinatura
az provider register --namespace Microsoft.Storage

# Cria a conta de armazenamento
if ! az storage account show --name "$storageAccountName" --resource-group "$resourceGroup" &>/dev/null; then
  az storage account create --resource-group "$resourceGroup" \
    --name "$storageAccountName" \
    --location "$location" \
    --sku Standard_LRS
else
  echo "A conta de armazenamento '$storageAccountName' ja existe"
fi

## Recupera o Token da Conta de Armazenamento
connection_string=$(az storage account show-connection-string --name $storageAccountName --resource-group $resourceGroup --query connectionString --output tsv)

# Cria o compartilhamento de arquivos 
if ! az storage share exists --name "$file_share_name" --account-name "$storageAccountName" --connection-string "$connection_string" | grep true; then
  az storage share create --name "$file_share_name" --account-name "$storageAccountName" --connection-string "$connection_string"
else
  echo "O compartilhamento de arquivos '$file_share_name' ja existe"
fi