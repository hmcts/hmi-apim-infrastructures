locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
}

module "apim_apis" {
  source     = "git@github.com:hmcts/terraform-module-apim-api?ref=master"
  env        = var.env
  product    = var.product
  department = var.department

  api_name           = var.product_name
  api_protocols      = ["http", "https"]
  api_content_format = "openapi+json"
  api_content_value = replace(file("${path.module}/resources/api-spec/hmi-api-health.json"),
  "#apimUrl#", var.apim_url)
  api_service_url = "https://${var.apim_url}"

  policy_xml_content = replace(file("${path.module}/resources/policy-files/api-policy.xml"),
  "#oAuthRole#", var.oauth_role)
  api_operations = local.policy_file_template

  depends_on = [
    module.api_mgmt_product
  ]
}