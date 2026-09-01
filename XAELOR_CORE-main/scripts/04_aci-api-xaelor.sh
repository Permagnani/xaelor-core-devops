#!/bin/bash

# =========================================================
# 04_aci-api-xaelor.sh
# Sobe o Container da API Java (XAELOR_CORE) no ACI,
# conectando no MySQL que ja esta rodando em outro ACI
# =========================================================

# Variaveis
# ALTERE PARA SEU RM E SUA REGIAO (Politica)
rm=564995
location="canadacentral"
resourceGroup="rg-xaelor-core"
acrName="acrxaelor$rm"
aciName="564995-api-xaelor"
aciNameMysql="564995-mysql-xaelor"
imageName="564995-api-xaelor"
tag="v1"
keyVaultName="kv-xaelor-$rm"
mysqlURL=$(az container show --resource-group $resourceGroup --name $aciNameMysql --query ipAddress.fqdn --output tsv)

# Registra o Servico de ACI na Assinatura
az provider register --namespace Microsoft.ContainerInstance

# Deploy do Container da Api Java
az container create \
  --resource-group $resourceGroup \
  --name $aciName \
  --location $location \
  --image $acrName.azurecr.io/$imageName:$tag \
  --cpu 1 \
  --memory 1 \
  --os-type Linux \
  --dns-name-label api-xaelor-$rm \
  --ports 8082 \
  --registry-login-server $acrName.azurecr.io \
  --registry-username $(az keyvault secret show --vault-name $keyVaultName --name acr-username --query value -o tsv) \
  --registry-password $(az keyvault secret show --vault-name $keyVaultName --name acr-password --query value -o tsv) \
  --environment-variables \
    SPRING_DATASOURCE_URL=$(az keyvault secret show --name spring-datasource-url --vault-name $keyVaultName --query value -o tsv | sed "s/564995-mysql-xaelor/$mysqlURL/") \
    SPRING_DATASOURCE_USERNAME=$(az keyvault secret show --name spring-datasource-username --vault-name $keyVaultName --query value -o tsv) \
    SPRING_DATASOURCE_PASSWORD=$(az keyvault secret show --name spring-datasource-password --vault-name $keyVaultName --query value -o tsv) \
  --restart-policy Always

# O comando sed troca o placeholder 564995-mysql-xaelor pelo FQDN
# real do container MySQL em runtime (Somente Linux)

# Testes apos a criacao (descomente para usar)
#
# fqdnApi=$(az container show --resource-group rg-xaelor-core --name 564995-api-xaelor --query ipAddress.fqdn --output tsv)
#
# curl -X GET http://$fqdnApi:8082/perfume
#
# curl -X POST http://$fqdnApi:8082/perfume \
#   -H "Content-Type: application/json" \
#   -d '{
#     "nomePerfume": "Xaelor Noir",
#     "generoPerfume": "Unissex",
#     "descricaoPerfume": "Perfume amadeirado"
#   }'
#
# curl -X PUT http://$fqdnApi:8082/perfume/atualizar/1 \
#   -H "Content-Type: application/json" \
#   -d '{
#     "nomePerfume": "Xaelor Noir Intense",
#     "generoPerfume": "Unissex",
#     "descricaoPerfume": "Versao intensa"
#   }'
#
# curl -X DELETE http://$fqdnApi:8082/perfume/delete/1