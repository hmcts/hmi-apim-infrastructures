locals {
  apim_api_name           = "sds-api-mgmt-${var.env}"
  api_policy_raw = file("./resources/policy-files/api-policy.xml")
  api_resource_group = "ss-${var.env}-network-rg"
  api_mgmt_api_name       = "${var.product}-${var.component}-api"
  api_base_path           = var.product
}

module "apim_api" {
  count  = local.deploy_apim
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api?ref=master"

  api_mgmt_name         = local.apim_api_name
  api_mgmt_rg           = local.api_resource_group
  display_name          = "HMI"
  name                  = local.api_mgmt_api_name
  path                  = "${var.product}/${var.component}"
  product_id            = data.azurerm_api_management_product.apim_product[0].product_id
  protocols             = ["http", "https"]
  revision              = "1"
  service_url           = "https://${local.base_url}"
  swagger_url           = file("./resources/api-spec/hmi-api-health.json")
  content_format        = "openapi+json"
  subscription_required = false
}

module "apim_api_policy" {
  count                  = local.deploy_apim
  source                 = "git@github.com:hmcts/cnp-module-api-mgmt-api-policy?ref=master"
  api_mgmt_name          = local.apim_name
  api_mgmt_rg            = local.apim_rg
  api_name               = local.apim_api_name
  api_policy_xml_content = local.api_policy_raw

  depends_on = [
    module.apim_api
  ]
}