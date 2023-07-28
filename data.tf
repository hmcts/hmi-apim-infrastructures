locals {

  resource_group_name = "${local.prefix}-sharedinfra-${var.env}-rg"
  key_vault_name      = "${local.prefix}-kv-${var.env}"
  pip_client_host = "pip-client-host"
  servicenow_host = "hmi-servicenow-host"
}

data "azurerm_client_config" "current" {}

data "azurerm_key_vault" "kv" {
  name                = local.key_vault_name
  resource_group_name = local.resource_group_name
}

data "azurerm_key_vault_secret" "pip_host" {
  count        = local.deploy_apim
  name         = local.pip_client_host
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "snow_host" {
  count        = local.deploy_apim
  name         = local.servicenow_host
  key_vault_id = data.azurerm_key_vault.kv.id
}