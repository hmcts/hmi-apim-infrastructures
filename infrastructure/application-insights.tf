module "application_insights" {
  source = "git@github.com:hmcts/terraform-module-application-insights?ref=4.x"

  env     = var.env
  product = var.product

  resource_group_name = local.apim_rg
  sampling_percentage = var.sampling_percentage
  common_tags         = var.common_tags
}

moved {
  from = azurerm_application_insights.appinsights
  to   = module.application_insights.azurerm_application_insights.this
}

# You should have something like this in your Terraform:
resource "azurerm_api_management_logger" "appinsights" {
  name                = "sds-api-mgmt-${local.env}-logger"
  api_management_name = local.apim_name
  resource_group_name = local.apim_rg

  application_insights {
    instrumentation_key = azurerm_application_insights.this.instrumentation_key
  }
}