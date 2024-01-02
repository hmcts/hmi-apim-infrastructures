locals {
  app_config_name = "app-config-${var.product}-${var.env}"
}

resource "azurerm_app_configuration" "app_conf" {
  name                = local.app_config_name
  resource_group_name = local.api_resource_group
  location            = var.location
}

data "azurerm_client_config" "current" {}

resource "azurerm_role_assignment" "appconf_dataowner" {
  scope                = azurerm_app_configuration.app_conf.id
  role_definition_name = "App Configuration Data Owner"
  principal_id         = data.azurerm_client_config.current.object_id
}

resource "azurerm_app_configuration_feature" "test" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  description            = "test description"
  name                   = "test-ackey"
  label                  = "test-ackeylabel"
  enabled                = true
}