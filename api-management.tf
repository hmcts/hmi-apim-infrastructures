locals {
  api_resource_group = "ss-${var.env}-network-rg"
  api-mgmt-prod-name = "${var.product}-product-${var.env}"
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
  remap_entries = [
    for p in local.policies_data.policies : {
      operation_id = p.operationId
      xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/${p.templateFile}"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", var.pih_host),
        "#snowHost#", var.snow_host)
      display_name = p.display_name
      method       = p.method
      url_template = p.url_template
      description  = each.value.description
    }
  ]
}

data "policy_file" "api_operation_template" {
  vars = {
    TO_BE_REPLACED = jsonencode(local.remap_entries)
  }
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
  api_operations = data.policy_file.api_operation_template.vars.TO_BE_REPLACED

  depends_on = [
    module.api_mgmt_product
  ]
}