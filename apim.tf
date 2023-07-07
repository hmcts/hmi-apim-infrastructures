locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api_mgmt_product_name   = "${var.product}-${var.component}-api"
  api_base_path           = var.product
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
      display_name = "Example Operation"
      method       = "GET"
      url_template = "/example"
      description  = "Operation as example"
    }
  ]
}