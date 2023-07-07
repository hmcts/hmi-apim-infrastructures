locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api_mgmt_product_name   = "${var.product}-${var.component}-api"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
  api_base_path           = var.product
}

module "api_mgmt_product" {
  source                = "git@github.com:hmcts/cnp-module-api-mgmt-product?ref=master"
  name                  = local.api-mgmt-prod-name
  approval_required     = "false"
  subscription_required = "false"
  api_mgmt_name         = local.apim_name
  api_mgmt_rg           = local.api_resource_group
}

module "apim_apis" {
  source      = "git::https://github.com/hmcts/terraform-module-apim-api?ref=master"
  env = var.env
  product     = var.product
  department  = "sds"

  api_name                  = local.api_mgmt_product_name
  api_revision              = "1"
  api_protocols             = ["https"]
  api_service_url           = "https://${local.base_url}"
  api_subscription_required = false
  api_content_format        = "openapi+json"
  api_content_value         = file("${path.module}/resources/api-spec/hmi-api-health.json")


  policy_xml_content = "<xml></xml>"
  api_operations = [
    {
      operation_id = "opt-1"
      xml_content  = "<xml></xml>"
      display_name = "Example Operation 1"
      method       = "GET"
      url_template = "/example1"
      description  = "Operation as example"
    },
    {
      operation_id = "opt-2"
      xml_content  = "<xml></xml>"
      display_name = "Example Operation 2"
      method       = "GET"
      url_template = "/example2"
      description  = "Operation as example"
    }
  ]

  depends_on = [
    module.api_mgmt_product
  ]
}