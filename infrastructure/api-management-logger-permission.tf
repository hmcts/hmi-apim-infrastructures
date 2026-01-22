resource "azurerm_role_assignment" "app_insight_viewers" {
  scope                = azurerm_api_management_api_diagnostic.api_logs[0].api_management_logger_id
  role_definition_name = "Application Insights Component Contributor"
  principal_id         = data.azuread_group.hmi-group.object_id
  count                = local.env == "prod" ? 0 : 1
}

resource "azurerm_role_assignment" "app_insight_viewers_monitoring" {
  scope                = module.application_insights[0].id
  role_definition_name = "Monitoring Reader"
  principal_id         = data.azuread_group.hmi-group.object_id
  count                = local.env == "prod" ? 0 : 1
}

data "azuread_group" "hmi-group" {
  display_name = "DTS HMI App Insights"
}


resource "azurerm_role_assignment" "app_insight_viewers_prod" {
  scope                = azurerm_api_management_api_diagnostic.api_logs[0].api_management_logger_id
  role_definition_name = "Application Insights Component Contributor"
  principal_id         = data.azuread_group.hmi-group-prod.object_id
  count                = local.env == "prod" ? 1 : 0
}

resource "azurerm_role_assignment" "app_insight_viewers_monitoring_prod" {
  scope                = data.azurerm_application_insights.sds_app_insights.id
  role_definition_name = "Monitoring Reader"
  principal_id         = data.azuread_group.hmi-group-prod.object_id
  count                = local.env == "prod" ? 1 : 0
}

data "azuread_group" "hmi-group-prod" {
  display_name = "DTS HMI App Insights (PROD)"
}