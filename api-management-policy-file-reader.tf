locals {
  remap_entries = var.env
}

data "policy_file" "api_operation_template" {
  TO_BE_REPLACED = local.remap_entries
}