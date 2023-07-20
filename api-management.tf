locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
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
      for_each = { for p in local.policies_data.policies : p.operationId => p }
      operation_id = each.value.operationId
      xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/${each.value.templateFile}"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", var.pih_host),
        "#snowHost#", var.snow_host)
      display_name = each.value.display_name
      method       = each.value.method
      url_template = each.value.url_template
      description  = each.value.description
    }
  ]

  depends_on = [
    module.api_mgmt_product
  ]
}