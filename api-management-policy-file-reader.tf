locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
  policies = [for policy in local.policies_data.policies : policy]
}

data "policy_file" "api_operation_template" {
  remap_entries = [
      for policy in local.policies : {

        operation_id = policy.operationId
        xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/${policy.templateFile}"),
          "#keyVaultHost#", var.key_vault_host),
          "#pihHost#", var.pih_host),
          "#snowHost#", var.snow_host)
        display_name = policy.display_name
        method       = policy.method
        url_template = policy.url_template
        description  = policy.description
      }
    ]
}