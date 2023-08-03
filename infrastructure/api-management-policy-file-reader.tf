locals {
  policies_data = jsondecode(file("${path.module}/resources/policy-files/policies.json"))

  policy_file_template = [for policy in local.policies_data.policies :
    {
      operation_id = policy.operationId
      xml_content  = replace(replace(replace(file("${path.module}/resources/policy-files/${policy.templateFile}"),
        "#keyVaultHost#", var.key_vault_host),
        "#pihHost#", length(data.azurerm_key_vault_secret.pip_host) > 0 ? data.azurerm_key_vault_secret.pip_host[0].value : ""),
        "#snowHost#", length(data.azurerm_key_vault_secret.snow_host) > 0 ? data.azurerm_key_vault_secret.snow_host[0].value : "")
      display_name = policy.display_name
      method       = policy.method
      url_template = policy.url_template
      description  = policy.description
      headers = can(policy.headers)  ? [for header in policy.headers :
        {
          name = header.name
          required = header.required
          type = header.type
          default_value = header.default_value
        }
      ] : null
      response = can(policy.response) ? {
        status_code = policy.response.status_code
        description = policy.response.description
      } : null
      query_parameters =  can(policy.query_parameters) ? [for query_parameter in policy.query_parameters :
      {
        name = query_parameter.name
        required = query_parameter.required
        type = query_parameter.type
        default_value = query_parameter.default_value
      }
      ] : null
      template_parameter =  can(policy.template_parameter) ? {
        name = policy.template_parameter.name
        required = policy.template_parameter.required
        type = policy.template_parameter.type
        default_value = policy.template_parameter.default_value
      } : null
      tag = can(policy.tag) ? {
        name = policy.tag.name
        display_name = policy.tag.display_name
      } : null
    }
  ]
}