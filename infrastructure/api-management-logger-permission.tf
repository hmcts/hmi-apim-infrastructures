resource "azurerm_role_assignment" "app_insight_viewers" {
  scope                = azurerm_api_management_api_diagnostic.api_logs[0].api_management_logger_id
  role_definition_name = "Application Insights Component Contributor"
  principal_id         = data.azuread_group.hmi-group.id
  count                = local.env == "prod" ? 0 : 1
}

resource "azurerm_role_assignment" "app_insight_viewers_monitoring" {
  scope                = data.azurerm_application_insights.sds_app_insights.id
  role_definition_name = "Monitoring Reader"
  principal_id         = data.azuread_group.hmi-group.id
  count                = local.env == "prod" ? 0 : 1
}

data "azuread_group" "hmi-group" {
  display_name = "DTS HMI App Insights"
}