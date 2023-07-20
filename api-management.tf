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
  api_operations = [
    {
      operation_id = "update-publication"
      xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/CaTH/api-op-publication-policy.xml"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", var.pih_host),
        "#snowHost#", var.snow_host)
      display_name = "Publication"
      method       = "POST"
      url_template = "/pih/publication"
      description  = "Publication of an artefact"
    },
    {
      operation_id = "publication-health"
      xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/CaTH/api-op-publication-health-policy.xml"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", var.pih_host),
        "#snowHost#", var.snow_host)
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