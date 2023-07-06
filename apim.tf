locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api_mgmt_product_name   = "${var.product}-${var.component}-api"
  api_base_path           = var.product
}

module "api_mgmt_product" {
  source                = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  name                  = local.api_mgmt_product_name
  approval_required     = "false"
  subscription_required = "false"
  api_mgmt_name         = local.apim_name
  api_mgmt_rg           = local.api_resource_group
}

module "apim_api" {
  count  = local.deploy_apim
  source = "git@github.com:hmcts/cnp-module-api-mgmt-api?ref=master"

  api_mgmt_name         = local.apim_name
  api_mgmt_rg           = local.api_resource_group
  display_name          = local.api_mgmt_product_name
  name                  = local.api_mgmt_product_name
  path                  = "${var.product}/${var.component}"
  product_id            = module.api_mgmt_product.product_id
  protocols             = ["http", "https"]
  revision              = "1"
  service_url           = "https://${local.base_url}"
  swagger_url           = file("${path.module}/resources/api-spec/hmi-api-health.json")
  content_format        = "openapi+json"
  subscription_required = false
}

module "apim_api_policy" {
  count                  = local.deploy_apim
  source                 = "git@github.com:hmcts/cnp-module-api-mgmt-api-policy?ref=master"

  api_mgmt_name          = local.apim_name
  api_mgmt_rg            = local.apim_rg
  api_name               = local.api_mgmt_product_name
  api_policy_xml_content = file("${path.module}/resources/policy-files/api-policy.xml")

  depends_on = [
    module.apim_api
  ]
}