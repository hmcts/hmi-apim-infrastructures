locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
  policies = [for policy in local.policies_data.policies : policy]
}

data "policy_file" "api_operation_template" {
  remap_entries = [
      for policy in local.policies : {

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
}