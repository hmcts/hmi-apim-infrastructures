terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "3.117.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "~> 1.15.0"
    }
  }
}