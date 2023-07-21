locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))

  policy_file_template = [for policy in local.policies_data.policies :
  {
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