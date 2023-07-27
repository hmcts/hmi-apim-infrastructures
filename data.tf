locals {

  resource_group_name = "${local.prefix}-${var.env}-rg"
  key_vault_name      = "${local.prefix}-kv-${var.env}"
}

data "azurerm_client_config" "current" {}

data "azurerm_key_vault" "kv" {
  name                = local.key_vault_name
  resource_group_name = local.resource_group_name
}

data "azurerm_key_vault_secret" "pip_host" {
  count        = local.deploy_apim
  name         = "pih_host"
  key_vault_id = data.azurerm_key_vault.kv.id
}

data "azurerm_key_vault_secret" "snow_host" {
  count        = local.deploy_apim
  name         = "snow_host"
  key_vault_id = data.azurerm_key_vault.kv.id
}