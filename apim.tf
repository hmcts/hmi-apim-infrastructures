locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
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
  department  = var.department

  api_name                  = var.product_name
  api_revision              = "1"
  api_protocols             = ["http", "https"]
  api_service_url           = "https://${local.base_url}"
  api_subscription_required = false
  api_content_format        = "openapi+json"
  api_content_value         = file("${path.module}/resources/api-spec/hmi-api-health.json")

  policy_xml_content = file("${path.module}/resources/policy-files/api-policy.xml")
  api_operations = [
    {
      operation_id = "update-publication"
      xml_content  = file("${path.module}/resources/policy-files/CaTH/publication.xml")
      display_name = "Publication"
      method       = "POST"
      url_template = "/pih/publication"
      description  = "Publication of an artefact"
    },
    {
      operation_id = "publication-health"
      xml_content  = file("${path.module}/resources/policy-files/CaTH/health-check.xml")
      display_name = "Publication Health"
      method       = "GET"
      url_template = "/pih/health"
      description  = "Health check for CaTH"
    }
  ]

  depends_on = [
    module.api_mgmt_product
  ]
}