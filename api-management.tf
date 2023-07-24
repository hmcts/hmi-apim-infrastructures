locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
}

module "apim_apis" {
  source      = "git@github.com:hmcts/terraform-module-apim-api?ref=master"
  env = var.env
  product     = var.product
  department  = var.department

  api_name                  = var.product_name
  api_revision              = "1"
  api_protocols             = ["http", "https"]
  api_service_url           = "https://${local.base_url}"
  api_subscription_required = false
  api_content_format        = "swagger-link-json"
  api_content_value         = "https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/specs/future-hearings-hmi-api.json"

  policy_xml_content = file("${path.module}/resources/policy-files/api-policy.xml")
  api_operations = local.policy_file_template

  request {
    dynamic "header" {
      for_each = local.policy_file_template.headers
      content {
        name     = header.value["name"]
        required = header.value["required"]
        type     = header.value["type"]
      }
    }
  }

  depends_on = [
    module.api_mgmt_product
  ]
}