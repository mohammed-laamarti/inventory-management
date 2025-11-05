# 📋 RÉCAPITULATIF COMPLET - Jenkinsfiles pour tous les services

## ✅ TOUS LES JENKINSFILES CRÉÉS

Votre projet d'inventory management dispose maintenant de **6 Jenkinsfiles** pour un déploiement CI/CD complet :

### Services d'Infrastructure (3)
| Service | Jenkinsfile | Port | Rôle | Ordre de démarrage |
|---------|-------------|------|------|-------------------|
| ✅ Config Server | `config-server/Jenkinsfile` | 8888 | Configuration centralisée | 1️⃣ Premier |
| ✅ Discovery (Eureka) | `discovery/Jenkinsfile` | 8761 | Service Registry | 2️⃣ Deuxième |
| ✅ Gateway | `gateway/Jenkinsfile` | 8222 | API Gateway (LoadBalancer) | 3️⃣ Troisième |

### Microservices Métier (3)
| Service | Jenkinsfile | Port | Database | Kafka | Ordre |
|---------|-------------|------|----------|-------|-------|
| ✅ Product | `product/Jenkinsfile` | 8090 | PostgreSQL | ❌ | 4️⃣ |
| ✅ Supplier | `supplier/Jenkinsfile` | 8091 | MongoDB | ✅ | 4️⃣ |
| ✅ Raw Material | `raw-material/Jenkinsfile` | 8092 | PostgreSQL | ✅ | 4️⃣ |

---

## 🎯 ARCHITECTURE CI/CD

```
┌─────────────────────────────────────────────────────────┐
│                    JENKINS CI/CD                         │
└─────────────────────────────────────────────────────────┘
                            │
                    ┌───────┴────────┐
                    │   Git Push     │
                    └───────┬────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ Config-Server│    │  Discovery   │    │   Gateway    │
│  Pipeline    │───▶│   Pipeline   │───▶│   Pipeline   │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       │            ┌──────┴──────┬───────────┬┘
       │            │             │           │
       │            ▼             ▼           ▼
       │    ┌──────────────┐ ┌──────────┐ ┌──────────────┐
       │    │   Product    │ │ Supplier │ │ Raw Material │
       │    │   Pipeline   │ │ Pipeline │ │   Pipeline   │
       │    └──────┬───────┘ └────┬─────┘ └──────┬───────┘
       │           │              │              │
       └───────────┴──────────────┴──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │  Build Maven │
                    └──────┬───────┘
                            │
                            ▼
                    ┌──────────────┐
                    │ Build Docker │
                    └──────┬───────┘
                            │
                            ▼
                    ┌──────────────┐
                    │  Scan Trivy  │
                    └──────┬───────┘
                            │
                            ▼
                    ┌──────────────┐
                    │  Push to ACR │
                    └──────┬───────┘
                            │
                            ▼
                    ┌──────────────┐
                    │ Deploy to AKS│
                    └──────────────┘
```

---

## 🔄 ORDRE DE DÉPLOIEMENT RECOMMANDÉ

### Déploiement Initial (première fois)

**Étape 1 : Infrastructure K8s de base**
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/kafka-deployment.yaml
```

**Étape 2 : Services d'infrastructure (dans l'ordre)**
```bash
# 1. Config Server (DOIT être démarré en premier)
kubectl apply -f k8s/config-server-deployment.yaml
# Attendre qu'il soit prêt
kubectl wait --for=condition=ready pod -l app=config-server -n inventory --timeout=300s

# 2. Discovery (Eureka)
kubectl apply -f k8s/discovery-deployment.yaml
# Attendre qu'il soit prêt
kubectl wait --for=condition=ready pod -l app=discovery -n inventory --timeout=300s

# 3. Gateway
kubectl apply -f k8s/gateway-deployment.yaml
```

**Étape 3 : Microservices métier (peuvent être déployés en parallèle)**
```bash
kubectl apply -f k8s/product-deployment.yaml
kubectl apply -f k8s/supplier-deployment.yaml
kubectl apply -f k8s/raw-material-deployment.yaml
```

### Déploiements suivants (via Jenkins)

Une fois les pipelines Jenkins configurés, l'ordre de build recommandé :

1️⃣ **Config Server** → Fournit la configuration à tous
2️⃣ **Discovery** → Enregistre tous les services
3️⃣ **Gateway** → Route les requêtes vers les services
4️⃣ **Microservices** → Peuvent être déployés en parallèle

---

## 🛠️ CONFIGURATION JENKINS - 6 PIPELINES

### Option A : Multibranch Pipeline (RECOMMANDÉ)

Créez 6 pipelines multibranch dans Jenkins :

#### 1. Config Server Pipeline
- **Nom** : `config-server-pipeline`
- **Script Path** : `config-server/Jenkinsfile`
- **Priorité** : 🔴 HAUTE (démarrer en premier)

#### 2. Discovery Pipeline
- **Nom** : `discovery-pipeline`
- **Script Path** : `discovery/Jenkinsfile`
- **Priorité** : 🔴 HAUTE (après Config Server)

#### 3. Gateway Pipeline
- **Nom** : `gateway-pipeline`
- **Script Path** : `gateway/Jenkinsfile`
- **Priorité** : 🟡 MOYENNE

#### 4. Product Service Pipeline
- **Nom** : `product-service-pipeline`
- **Script Path** : `product/Jenkinsfile`
- **Priorité** : 🟢 NORMALE

#### 5. Supplier Service Pipeline
- **Nom** : `supplier-service-pipeline`
- **Script Path** : `supplier/Jenkinsfile`
- **Priorité** : 🟢 NORMALE

#### 6. Raw Material Service Pipeline
- **Nom** : `raw-material-service-pipeline`
- **Script Path** : `raw-material/Jenkinsfile`
- **Priorité** : 🟢 NORMALE

---

## 📊 TABLEAU DE COHÉRENCE

### Vérification avec Terraform ✅

| Variable Terraform | Valeur | Jenkinsfiles |
|-------------------|---------|--------------|
| `resource_group_name` | aks-Cloud | ✅ Tous alignés |
| `cluster_name` | aks | ✅ Tous alignés |
| `acr_name` | mohammed | ✅ Tous alignés |
| `location` | Sweden Central | ✅ OK |

### Vérification avec K8s Deployments ✅

| Service | K8s Deployment | Image K8s | Image Jenkinsfile | Match |
|---------|---------------|-----------|-------------------|-------|
| Config Server | config-server | mohammed.azurecr.io/config-server | mohammed.azurecr.io/config-server | ✅ |
| Discovery | discovery | mohammed.azurecr.io/discovery | mohammed.azurecr.io/discovery | ✅ |
| Gateway | gateway-service | mohammed.azurecr.io/gateway | mohammed.azurecr.io/gateway | ✅ |
| Product | product-service | mohammed.azurecr.io/product-service | mohammed.azurecr.io/product-service | ✅ |
| Supplier | supplier-service | mohammed.azurecr.io/supplier-service | mohammed.azurecr.io/supplier-service | ✅ |
| Raw Material | raw-material-service | mohammed.azurecr.io/raw-material-service | mohammed.azurecr.io/raw-material-service | ✅ |

---

## 🎨 DIFFÉRENCES ENTRE LES SERVICES

### Config Server (config-server/Jenkinsfile)
- 🔧 **Rôle** : Configuration centralisée
- 📝 **Note** : DOIT démarrer en premier
- 🔄 **Replicas** : 1 (single instance)
- 🌐 **Exposition** : ClusterIP (interne uniquement)

### Discovery - Eureka (discovery/Jenkinsfile)
- 🔧 **Rôle** : Service Registry
- 📝 **Note** : Démarre après Config Server
- 🔄 **Replicas** : 1 (single instance)
- 🌐 **Exposition** : ClusterIP (interne uniquement)
- 🔑 **Env Variable** : EUREKA_INSTANCE_HOSTNAME=discovery

### Gateway (gateway/Jenkinsfile)
- 🔧 **Rôle** : Point d'entrée principal (API Gateway)
- 📝 **Note** : Démarre après Config Server et Discovery
- 🔄 **Replicas** : 1 (peut être augmenté)
- 🌐 **Exposition** : **LoadBalancer** (IP PUBLIQUE) 🌍
- 🎯 **Port** : 8222
- 💡 **Spécial** : Affiche l'IP externe après déploiement

### Product Service (product/Jenkinsfile)
- 🔧 **Rôle** : Gestion des produits
- 💾 **Database** : PostgreSQL
- 📨 **Kafka** : Non
- 🔄 **Replicas** : 2
- 🌐 **Exposition** : ClusterIP

### Supplier Service (supplier/Jenkinsfile)
- 🔧 **Rôle** : Gestion des fournisseurs
- 💾 **Database** : MongoDB
- 📨 **Kafka** : Oui (Producer/Consumer)
- 🔄 **Replicas** : 2
- 🌐 **Exposition** : ClusterIP

### Raw Material Service (raw-material/Jenkinsfile)
- 🔧 **Rôle** : Gestion des matières premières
- 💾 **Database** : PostgreSQL
- 📨 **Kafka** : Oui (Producer/Consumer)
- 🔄 **Replicas** : 2
- 🌐 **Exposition** : ClusterIP

---

## 🚀 WORKFLOW AUTOMATIQUE COMPLET

### Développeur fait un git push

```bash
git add .
git commit -m "Update supplier service"
git push origin main
```

### Jenkins détecte et agit automatiquement

1. **Détection intelligente** : Jenkins détecte quel service a changé
2. **Build Maven** : Compilation du service concerné uniquement
3. **Build Docker** : Création de l'image avec tag (git commit hash)
4. **Scan sécurité** : Trivy analyse les vulnérabilités
5. **Push ACR** : Upload vers Azure Container Registry
6. **Deploy AKS** : Mise à jour automatique sur Kubernetes
7. **Vérification** : Validation du déploiement
8. **Rollback** : Si échec, retour à la version précédente automatique

---

## 📝 VARIABLES D'ENVIRONNEMENT COMMUNES

Tous les Jenkinsfiles partagent :

```groovy
ACR_NAME = 'mohammed'
ACR_URL = 'mohammed.azurecr.io'
AKS_CLUSTER = 'aks'
AKS_RESOURCE_GROUP = 'aks-Cloud'
K8S_NAMESPACE = 'inventory'
AZURE_CREDENTIALS_ID = 'azure-credentials'
IMAGE_TAG = git_commit_hash (7 premiers caractères)
```

---

## ⚙️ FONCTIONNALITÉS COMMUNES

Chaque Jenkinsfile inclut :

✅ **Smart Detection** - Skip si pas de changements
✅ **Maven Build** - Compilation Java
✅ **Docker Build** - Création image
✅ **Trivy Scan** - Analyse sécurité
✅ **Azure Login** - Authentification
✅ **ACR Push** - Upload image
✅ **AKS Deploy** - Déploiement K8s
✅ **Rollout Status** - Vérification
✅ **Auto Rollback** - Retour arrière si échec
✅ **Cleanup** - Nettoyage automatique

---

## 🔐 PRÉREQUIS JENKINS

### 1. Plugins Jenkins nécessaires
```
- Azure Credentials
- Azure CLI Plugin
- Docker Pipeline
- Pipeline
- Git
- Kubernetes CLI Plugin
- Maven Integration
- Pipeline: Stage View
```

### 2. Credentials Azure
Créez dans Jenkins avec ID : `azure-credentials`
- Subscription ID
- Client ID (App ID)
- Client Secret (Password)
- Tenant ID

### 3. Outils sur l'agent Jenkins
```
- Java 17
- Maven 3.6+
- Docker
- Azure CLI (az)
- kubectl
- Trivy (optionnel)
```

---

## 🌐 ACCÈS AUX SERVICES

### Via Gateway (Production)
```
Gateway IP externe → http://<EXTERNAL-IP>:8222

# Récupérer l'IP externe
kubectl get svc gateway-service -n inventory

# Routes
http://<EXTERNAL-IP>:8222/products/**      → Product Service
http://<EXTERNAL-IP>:8222/suppliers/**     → Supplier Service
http://<EXTERNAL-IP>:8222/raw-materials/** → Raw Material Service
```

### Accès direct (Debug/Dev)
```bash
# Port-forward pour accès direct
kubectl port-forward svc/config-server 8888:8888 -n inventory
kubectl port-forward svc/discovery 8761:8761 -n inventory
kubectl port-forward svc/product-service 8090:8090 -n inventory
kubectl port-forward svc/supplier-service 8091:8091 -n inventory
kubectl port-forward svc/raw-material-service 8092:8092 -n inventory
```

---

## 📊 MONITORING ET LOGS

### Voir les logs d'un service
```bash
# Config Server
kubectl logs -f deployment/config-server -n inventory

# Discovery
kubectl logs -f deployment/discovery -n inventory

# Gateway
kubectl logs -f deployment/gateway-service -n inventory

# Product
kubectl logs -f deployment/product-service -n inventory

# Supplier
kubectl logs -f deployment/supplier-service -n inventory

# Raw Material
kubectl logs -f deployment/raw-material-service -n inventory
```

### Vérifier le statut
```bash
# Tous les services
kubectl get all -n inventory

# Seulement les pods
kubectl get pods -n inventory

# Avec détails
kubectl describe pod <pod-name> -n inventory
```

---

## 🐛 TROUBLESHOOTING PAR SERVICE

### Config Server ne démarre pas
```bash
# Vérifier les logs
kubectl logs deployment/config-server -n inventory

# Vérifier si le pod est en cours de création
kubectl get pods -n inventory -l app=config-server

# Problème commun : git repository config inaccessible
```

### Discovery ne peut pas se connecter à Config Server
```bash
# Vérifier que Config Server est UP
kubectl get pods -n inventory -l app=config-server

# Vérifier les variables d'env
kubectl describe pod <discovery-pod> -n inventory | grep -A 10 Environment
```

### Gateway ne route pas correctement
```bash
# Vérifier l'enregistrement dans Eureka
kubectl port-forward svc/discovery 8761:8761 -n inventory
# Ouvrir http://localhost:8761

# Vérifier les routes
kubectl logs deployment/gateway-service -n inventory | grep -i route
```

### Services métier ne s'enregistrent pas
```bash
# Vérifier la connexion à Discovery
kubectl logs deployment/product-service -n inventory | grep -i eureka

# Vérifier les secrets de DB
kubectl get secrets -n inventory
kubectl describe secret postgres-secret -n inventory
kubectl describe secret mongo-secret -n inventory
```

---

## 🎉 RÉSULTAT FINAL

### Architecture complète avec CI/CD

✅ **6 Jenkinsfiles** fonctionnels et cohérents
✅ **6 Pipelines Jenkins** automatisés
✅ **Smart Deployment** avec détection de changements
✅ **Auto Rollback** en cas d'échec
✅ **Security Scanning** avec Trivy
✅ **Monitoring** avec Prometheus (annotations)
✅ **Service Discovery** avec Eureka
✅ **API Gateway** avec Spring Cloud Gateway
✅ **Configuration centralisée** avec Config Server

### À chaque git push :
1. Jenkins détecte le changement
2. Build et test automatiques
3. Création et scan de l'image Docker
4. Push vers Azure Container Registry
5. Déploiement automatique sur AKS
6. Vérification et rollback si nécessaire

**Tout est automatisé ! 🚀**

---

## 📋 CHECKLIST FINALE

- [ ] 6 Jenkinsfiles créés et vérifiés
- [ ] Credentials Azure configurés dans Jenkins (ID: azure-credentials)
- [ ] 6 Pipelines Jenkins créés
- [ ] Namespace `inventory` existe dans K8s
- [ ] Secrets `postgres-secret` et `mongo-secret` créés
- [ ] Infrastructure K8s déployée (Postgres, Mongo, Kafka)
- [ ] Ordre de déploiement respecté (Config → Discovery → Gateway → Services)
- [ ] Tests de connexion via Gateway
- [ ] Monitoring Prometheus configuré

---

**🎯 Tous vos services ont maintenant un pipeline CI/CD complet et automatisé !**

