locals {
  app_config_name = "${var.product}-app-config-${var.env}"
}

data "azurerm_resource_group" "test" {
  name     = local.api_resource_group
}

resource "azurerm_role_assignment" "app_conf_data_owner" {
  scope                = data.azurerm_resource_group.test.id
  role_definition_name = "App Configuration Data Owner"
  principal_id         = data.azurerm_client_config.current.object_id
}

resource "time_sleep" "role_assignment_sleep" {
  create_duration = "60s"

  triggers = {
    role_assignment = azurerm_role_assignment.app_conf_data_owner.id
  }
}
resource "azurerm_app_configuration" "app_conf" {
  name                = local.app_config_name
  resource_group_name = local.api_resource_group
  location            = var.location
  sku                 = "standard"

  depends_on = [
    azurerm_role_assignment.app_conf_data_owner,
    time_sleep.role_assignment_sleep
  ]
}

resource "azurerm_app_configuration_feature" "test_feature" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  description            = "test description"
  name                   = "test-ackey"
  label                  = "test-ackeylabel"
  enabled                = true
}