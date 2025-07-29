terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.37.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 2.5.0"
    }
  }
}