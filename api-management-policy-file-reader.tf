locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
  remap_entries = [
  ]
}

data "policy_file" "api_operation_template" {
  TO_BE_REPLACED = local.remap_entries
}