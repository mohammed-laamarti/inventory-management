variable "location" {
  description = "Azure region"
  default     = "Sweden Central"
}

variable "resource_group_name" {
  description = "Nom du resource group"
  default     = "aks-Cloud"
}

variable "cluster_name" {
  description = "Nom du cluster AKS"
  default     = "aks"
}

variable "acr_name" {
  description = "Nom du Azure Container Registry existant"
  default     = "mohammed"
}

variable "node_count" {
  description = "Nombre de nœuds du cluster"
  default     = 2
}

variable "node_vm_size" {
  description = "Type de machine virtuelle pour les nœuds"
  default     = "Standard_B2s"
}

variable "environment" {
  description = "Environnement (dev, staging, prod)"
  default     = "dev"
}
