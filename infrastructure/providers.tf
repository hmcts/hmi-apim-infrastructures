terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      version = "4.4.0"
    }
  }
}