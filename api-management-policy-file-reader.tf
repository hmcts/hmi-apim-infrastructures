locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))
}

output "policy_template_text" {
  value = [for policy in local.policies_data.policies :
    {
      operation_id = policy.operationId
      display_name = policy.display_name
      method       = policy.method
      url_template = policy.url_template
      description  = policy.description
    }
  ]
}