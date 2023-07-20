locals {
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
  TO_BE_REPLACED = jsonencode(local.remap_entries)
}