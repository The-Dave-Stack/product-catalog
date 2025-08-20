# Multi-Environment Deployment & Configuration

This project is designed to run seamlessly across multiple environments: **local**, **staging**, and **production**. Each environment has optimized configurations for its specific use case.

## 📊 Environment Overview

| Environment | Spring Profile | Docker Compose File | Database | Usage |
|-------------|---------------|---------------------|----------|--------|
| **Local** | `local,docker` | `docker-compose.local.yml` | `product_catalog_local` | Development with full debugging |
| **Stage** | `stage,docker` | `docker-compose.stage.yml` | `product_catalog_stage` | General staging with nginx proxy |
| **Prod** | `prod,docker` | `docker-compose.prod.yml` | `product_catalog_prod` | General production with nginx proxy |
| **VPS Stage** | `stage,docker` | `docker-compose.vps-stage.yml` | `product_catalog_stage` | **VPS-specific staging with Traefik** |

## 🚀 Docker Compose File Categories

#### General Use (Self-Hosted/Traditional Deployments)
- `docker-compose.local.yml` - Local development environment
- `docker-compose.stage.yml` - General staging with nginx reverse proxy
- `docker-compose.prod.yml` - General production with nginx reverse proxy and SSL

#### VPS-Specific (Traefik + Dokploy Deployments)
- `docker-compose.vps-stage.yml` - **VPS staging with Traefik integration**
  - Designed for Dokploy deployment platforms
  - Uses external `dokploy-network` for Traefik routing
  - No nginx required - Traefik handles all routing

## 🚀 Environment-Specific Docker Commands

#### Local Development
```bash
# Start local development environment
docker compose -f docker-compose.local.yml up -d

# Stop services
docker compose -f docker-compose.local.yml down
````

#### Stage Environment

```bash
# Start staging environment
docker compose -f docker-compose.stage.yml up -d

# Start with nginx reverse proxy
docker compose -f docker-compose.stage.yml --profile nginx up -d

# Stop services  
docker compose -f docker-compose.stage.yml down
```

#### Production Environment

```bash
# Start production environment
docker compose -f docker-compose.prod.yml up -d

# Start with nginx reverse proxy and SSL
docker compose -f docker-compose.prod.yml --profile nginx up -d

# Scale for high availability
docker compose -f docker-compose.prod.yml up -d --scale product-catalog=3

# Stop services
docker compose -f docker-compose.prod.yml down
```

#### VPS Stage Environment (Traefik + Dokploy)

```bash
# Deploy to VPS with Traefik
docker compose -f docker-compose.vps-stage.yml up -d

# Monitor VPS deployment
docker compose -f docker-compose.vps-stage.yml logs -f product-catalog-stage

# Stop VPS services
docker compose -f docker-compose.vps-stage.yml down
```

## 📝 Environment Configuration Files

Each environment requires its own `.env` file for secrets and configuration.

  - **`.env.local.example`** -\> `.env.local`
  - **`.env.stage.example`** -\> `.env.stage`
  - **`.env.prod.example`** -\> `.env.prod`

Copy the appropriate `.env.{environment}.example` file and customize the values before deployment.

### Required Variables by Environment

**Local Environment (`.env.local`)**
| Variable | Description | Example | Required |
|----------|-------------|---------|----------|
| `DATABASE_NAME` | Local database name | `product_catalog_local` | ✅ |
| `DATABASE_USERNAME` | Local database user | `local_user` | ✅ |
| `DATABASE_PASSWORD` | Local database password | `local_password` | ✅ |
| `JWT_SECRET` | JWT signing key for local | `localDevSecretKey...` | ✅ |

**Stage Environment (`.env.stage`)**
| Variable | Description | Example | Required |
|----------|-------------|---------|----------|
| `DATABASE_NAME` | Stage database name | `product_catalog_stage` | ✅ |
| `DATABASE_USERNAME` | Stage database user | `stage_user` | ✅ |
| `DATABASE_PASSWORD` | Stage database password | `secure-stage-password` | ✅ |
| `JWT_SECRET` | JWT signing key for stage | `changeme-stage-secret...` | ✅ |

**Production Environment (`.env.prod`)**
| Variable | Description | Example | Required |
|----------|-------------|---------|----------|
| `DATABASE_NAME` | Production database name | `product_catalog_prod` | ✅ |
| `DATABASE_USERNAME` | Production database user | `prod_user` | ✅ |
| `DATABASE_PASSWORD` | Production database password | `very-secure-prod-password` | ✅ |
| `JWT_SECRET` | Production JWT signing key | `changeme-production-secret...` | ✅ |

## 🎯 Git Flow Integration

The multi-environment setup integrates seamlessly with Git Flow:

  - **Feature branches** → Local development and testing
  - **develop branch** → Automated stage deployment
  - **main branch** → Automated production deployment

All environments are validated by GitHub Actions to ensure consistency and prevent configuration drift.

## ⚠️ Important Migration Note

**Breaking Change**: The default `docker-compose.yml` has been renamed to `docker-compose.local.yml`.

**Before** (deprecated): `docker compose up`
**After** (current): `docker compose -f docker-compose.local.yml up -d`

This change ensures a clear separation between environments.