locals {
  app_config_name = "${var.product}-app-config-${var.env}"
}

data "azurerm_resource_group" "rg" {
  name     = local.api_resource_group
  location = var.location
}

data "azurerm_client_config" "test" {
}

resource "azurerm_role_assignment" "app_conf_data_owner" {
  scope                = data.azurerm_resource_group.rg.id
  role_definition_name = "App Configuration Data Owner"
  principal_id         = data.azurerm_client_config.test.object_id
}

resource "azurerm_app_configuration" "app_conf" {
  name                = local.app_config_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "standard"

  depends_on = [
    azurerm_role_assignment.app_conf_data_owner,
  ]
}

resource "azurerm_app_configuration_feature" "test_feature" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  description            = "test description"
  name                   = "test-ackey"
  label                  = "test-ackeylabel"
  enabled                = true
}