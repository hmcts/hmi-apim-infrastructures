terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.56.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 2.5.0"
    }
  }
}