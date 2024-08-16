terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "3.116.0"
    }
  }
}