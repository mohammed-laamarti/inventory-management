##########################################
# Provider Configuration
##########################################
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  features {}
  subscription_id = "5e79ae41-77eb-488b-a90e-b266db31548e"
}
