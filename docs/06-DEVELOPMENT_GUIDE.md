# Development Guide

This comprehensive guide covers everything developers need to know about contributing to the Product Catalog project, including our automated workflows, development practices, and troubleshooting tips.

## 🚀 Development Workflow

### Three-Branch Git Flow Strategy

Our project uses a **three-branch Git Flow strategy** designed for robust development and automated deployment:

```
Feature Branches → integration → develop → main
     ↓               ↓           ↓         ↓
   Local Dev    Auto-PR Creation  Stage   Production
```

#### Branch Structure

| Branch | Purpose | Protection | Automation |
|--------|---------|------------|------------|
| **`integration`** | Feature integration & testing | ✅ Status checks + admin push access | ⚡ Auto-creates PR to develop |
| **`develop`** | Stage deployment preparation | 🔒 **PR-only** + reviews + status checks | 🚀 Triggers stage deployment + auto-PR to main |
| **`main`** | Production releases | 🔒 **PR-only** + reviews + status checks | 🏭 Triggers production deployment |

#### Development Process

**1. 🌟 Start New Feature**
```bash
# Always branch from integration (not develop!)
git checkout integration
git pull origin integration
git checkout -b feature/your-feature-name
```

**2. 🔧 Develop & Test Locally**
```bash
# Make your changes
git add .
git commit -m "feat: add new feature functionality"

# Test locally
docker compose -f docker-compose.local.yml up -d
mvn test
mvn verify -Pe2e-tests
```

**3. 🔄 Integrate Feature**
```bash
# Push feature branch and merge to integration
git push origin feature/your-feature-name
# Create PR: feature/your-feature-name → integration
# After approval, merge to integration
```

**4. ⚡ Automatic Promotion (NEW!)**
When you push to `integration`, our **Auto-PR System** automatically:
- ✅ Runs comprehensive CI tests (unit, integration, formatting, environment validation)
- 🤖 **Auto-creates PR** from `integration` → `develop` if CI passes
- 📝 Includes detailed CI summary and commit information
- 🔄 Updates existing PR with new results if one already exists

**5. 🎯 Stage Deployment & Auto-PR**
- Review the auto-generated PR from `integration` → `develop`
- Approve and merge to trigger stage deployment
- **Automatic**: Stage deployment creates auto-PR from `develop` → `main`
- Test in staging environment and validate deployment

**6. 🚀 Production Release**
- Review the auto-generated PR from `develop` → `main`
- Final review and approval with production checklist
- Merge triggers production deployment with semantic versioning

## ⚡ Automated CI/CD Workflows

### 🤖 Dual Auto-PR Creation System

Our **complete automation chain** revolutionizes the development process with **two automated PR workflows**:

#### Integration → Develop Workflow (`integration-cd.yml`)
1. **Trigger**: Push to `integration` branch
2. **CI Testing**: Comprehensive validation (unit tests, integration tests, formatting, environment config)
3. **Auto-PR Logic**:
   - ✅ **CI Success** → Automatically creates PR to `develop`
   - ❌ **CI Failure** → No PR created, developer notified
   - 🔄 **Existing PR** → Adds update comment instead of creating duplicate

#### Develop → Main Workflow (`develop-cd.yml`)
1. **Trigger**: Successful merge to `develop` branch
2. **Stage Deployment**: RC versioning, Docker build, stage environment deployment
3. **Auto-PR Logic**:
   - ✅ **Stage Success** → Automatically creates PR to `main`
   - 📊 **Deployment Info** → Includes RC version, Docker tags, test results
   - 🔄 **Existing PR** → Adds deployment update comment with latest stage results

#### What to Expect

**Integration → Develop PR Title:**
```
🚀 Integration → Develop: Auto-promotion after CI success
```

**Develop → Main PR Title:**
```
🚀 Production Release: Deploy v1.0.0-RC.123 to Production
```

**Integration → Develop PR Description:**
```markdown
## 🎯 Automated Promotion from Integration

This PR was automatically created after successful CI validation.

### ✅ Validation Summary
- Unit Tests: ✅ Passed  
- Integration Tests: ✅ Passed
- Code Formatting: ✅ Passed (Spotless)
- Environment Config: ✅ Validated
- Build: ✅ Successful

### 📋 Changes
- Commits: 3 new commit(s) since last develop merge
- Latest commit: feat: add new product filtering

### 🔗 CI Details
- Workflow run: [link to GitHub Actions]
- Triggered by: @developer-username
- Completed: 2024-01-15 14:30:22 UTC
```

**Develop → Main PR Description:**
```markdown
## 🎯 Production Deployment Request

This PR was automatically created after successful stage deployment and validation.

### ✅ Stage Deployment Summary
- **RC Version**: 1.0.0-RC.123
- **Docker Images**: Built and pushed to registry
- **Environment**: Stage deployment completed successfully
- **Tests**: All tests passed (unit, integration, stage validation)
- **Pre-release**: Created with tag v1.0.0-RC.123

### 📋 Changes for Production
- **Commits**: 5 new commit(s) since last main branch
- **Latest commit**: feat: enhance product filtering capabilities
- **Docker Image**: `ghcr.io/the-dave-stack/product-catalog:stage`

### 🚀 Production Checklist
- [x] Stage deployment successful
- [x] All automated tests passed
- [x] Docker images built and available
- [x] Pre-release created
- [ ] Manual verification in stage environment
- [ ] Production deployment approval
```

#### Developer Responsibilities

**For Integration → Develop PRs:**
1. **Review Auto-Generated PRs**: Don't just merge automatically
2. **Test in Integration**: Ensure your changes work before pushing
3. **Monitor CI Status**: Check GitHub Actions for any failures
4. **Handle Failures**: Fix issues if CI fails (no PR will be created)

**For Develop → Main PRs:**
1. **Validate Stage Deployment**: Verify the RC version works correctly in stage
2. **Review Production Checklist**: Ensure all deployment criteria are met
3. **Manual Testing**: Perform critical path testing in stage environment
4. **Monitor Deployment Metrics**: Check stage environment health and performance
5. **Rollback Preparation**: Understand rollback procedures before production merge

### 📊 All CI/CD Workflows

| Workflow | Trigger | Purpose | Key Actions |
|----------|---------|---------|-------------|
| **pr-validation.yml** | PRs to main/develop/integration | Validate changes | Tests, formatting, environment validation |
| **integration-cd.yml** ⭐ | Push to integration | Auto-PR creation | CI tests + auto-create PR to develop |
| **develop-cd.yml** ⭐ | Push to develop | Stage deployment + Auto-PR | RC versioning, Docker build, stage deploy, auto-create PR to main |
| **main-cd.yml** | Push to main | Production deployment | Semantic versioning, production deploy |
| **environment-validation.yml** | Scheduled/manual | Config validation | Environment consistency checks |

## 🛠️ Development Environment

### Local Setup (Enhanced)

**Prerequisites:**
- Java 21 JDK
- Docker Desktop
- Maven 3.9+
- Git
- IDE (IntelliJ IDEA recommended)

**Quick Start:**
```bash
# 1. Clone and navigate
git clone https://github.com/The-Dave-Stack/product-catalog.git
cd product-catalog

# 2. Set up environment
cp .env.local.example .env.local

# 3. Start development environment
docker compose -f docker-compose.local.yml up -d

# 4. Verify setup
curl http://localhost:8080/actuator/health
```

### IDE Configuration

**IntelliJ IDEA Settings:**
```bash
# Import as Maven project
# Enable annotation processing
# Set Java 21 as project SDK
# Install plugins: Spring Boot, Docker, Database Tools
```

**VS Code Extensions:**
- Extension Pack for Java
- Spring Boot Extension Pack
- Docker
- GitLens

### Code Quality Tools

**Spotless (Code Formatting):**
```bash
# Check formatting
mvn spotless:check

# Apply formatting
mvn spotless:apply
```

**Testing Commands:**
```bash
# Unit tests only
mvn test

# Integration tests only  
mvn test -Dtest=*IT

# E2E tests
mvn verify -Pe2e-tests

# All tests
mvn verify
```

## 🧪 Testing Strategy for Developers

### Test Layer Architecture

```
🌐 E2E Tests (10 tests)
├── Full application testing
├── RestAssured + Testcontainers
└── Authentication flows

🔗 Integration Tests (9 tests)  
├── Controller + Service + Database
├── @SpringBootTest + MockMvc
└── PostgreSQL via Testcontainers

⚡ Unit Tests (14 tests)
├── Service layer isolation
├── JUnit 5 + Mockito
└── Fast, focused testing
```

### Writing Tests Guidelines

**Unit Test Pattern:**
```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    @Test
    void shouldCreateProductSuccessfully() {
        // Given
        CreateProductDto createDto = new CreateProductDto(/* ... */);
        Product savedProduct = new Product(/* ... */);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        
        // When
        ProductResponseDto result = productService.createProduct(createDto);
        
        // Then
        assertThat(result.getName()).isEqualTo("Expected Name");
        verify(productRepository).save(any(Product.class));
    }
}
```

**Integration Test Pattern:**
```java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    
    @Test
    void shouldCreateProduct() throws Exception {
        String token = loginAsAdmin();
        
        mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }
}
```

### Test Coverage Expectations

| Layer | Minimum Coverage | Focus |
|-------|------------------|-------|
| **Service Layer** | 90%+ | Business logic, edge cases |
| **Controller Layer** | 80%+ | HTTP handling, validation |
| **Repository Layer** | 70%+ | Custom queries, complex operations |

## 📋 Development Best Practices

### Code Conventions

**Naming Conventions:**
- **Classes**: PascalCase (`ProductService`, `CreateProductDto`)
- **Methods**: camelCase (`createProduct`, `findByCategory`)
- **Variables**: camelCase (`productName`, `isActive`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)

**Package Structure:**
```
com.thedavestack.productcatalog/
├── controller/     # @RestController classes
├── service/        # @Service classes  
├── repository/     # @Repository interfaces
├── dto/           # Request/Response DTOs
├── model/         # @Entity classes
├── security/      # JWT, filters, config
├── exception/     # Custom exceptions
└── config/        # @Configuration classes
```

### Commit Message Guidelines

**Format:** `type(scope): description`

**Types:**
- `feat`: New feature
- `fix`: Bug fix  
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Examples:**
```bash
git commit -m "feat(products): add category filtering endpoint"
git commit -m "fix(auth): handle expired JWT tokens properly"
git commit -m "docs(api): update OpenAPI schema for products"
git commit -m "test(integration): add pagination test cases"
```

### PR Creation Best Practices

**PR Title Format:**
```
feat: Add product category filtering functionality
fix: Resolve JWT token expiration handling
docs: Update development workflow documentation
```

**PR Description Template:**
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature  
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Tests pass locally
- [ ] Documentation updated if needed
```

### Security Considerations

**Authentication Testing:**
```bash
# Test with admin credentials
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Use JWT token in requests
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/v1/products
```

**Security Checklist:**
- ✅ No hardcoded secrets in code
- ✅ Proper input validation
- ✅ JWT token expiration handling
- ✅ Role-based access control testing
- ✅ SQL injection prevention (use JPA properly)

## 🔐 Repository Setup for Auto-PR Creation

### Personal Access Token (PAT) Configuration

The auto-PR creation workflow requires a Personal Access Token with proper permissions to create pull requests on behalf of GitHub Actions.

#### Required Setup (Repository Owner):

**1. Create Personal Access Token:**
1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token" → "Generate new token (classic)"
3. Set expiration (recommend 90 days or 1 year)
4. Select required scopes:
   - ✅ `repo` (Full control of private repositories)
   - ✅ `pull_requests` (Access pull requests)
5. Generate token and **copy it immediately**

**2. Add Token to Repository Secrets:**
1. Go to Repository Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Name: `PAT_FOR_CI`
4. Value: [paste your PAT here]
5. Click "Add secret"

**3. Verify Setup:**
After adding the PAT secret, the next push to `integration` branch will automatically create PRs to `develop` if CI passes.

#### Alternative: Repository Permission Settings
If you prefer not to use a PAT, you can enable repository-wide permissions:
1. Repository Settings → Actions → General
2. Scroll to "Workflow permissions" 
3. Enable "Allow GitHub Actions to create and approve pull requests"

### AI-Assisted Development Workflow (Single Developer)

For **single-developer repositories using AI agents** (like Claude Code), special considerations apply to the automated workflow:

#### Admin Override for AI Workflows

**Problem**: GitHub's branch protection prevents PR creators from approving their own PRs, which blocks AI-assisted development where the same account creates and needs to merge PRs.

**Solution**: Configure branch protection with **admin override enabled**:

**1. Enhanced Branch Protection Configuration:**
- **`integration`**: Admin direct push access for rapid development
- **`develop` and `main`**: 🔒 **Push restrictions enabled** - NO direct commits allowed
- **Admin override**: `enforce_admins: false` allows admin to merge PRs when needed
- **Security**: All release branches (`develop`/`main`) force proper PR workflow

**2. Admin Override Process:**
```bash
# When auto-generated PR needs merging after validation
gh pr merge [PR_NUMBER] --admin --merge

# Example: Merge integration → develop PR after AI validation
gh pr merge 36 --admin --merge
```

**3. When to Use Admin Override:**
- ✅ AI-generated changes have been manually reviewed and validated
- ✅ All CI tests pass and code quality checks succeed
- ✅ Changes align with project requirements and security standards
- ❌ Never bypass review for complex or security-sensitive changes

**4. Safety Guidelines:**
```bash
# Always review the PR content before override
gh pr view [PR_NUMBER] --json title,body,additions,deletions

# Check CI status before merge
gh pr checks [PR_NUMBER]

# Verify no conflicts exist
gh pr status --repo [REPO_NAME]
```

**5. Alternative: Manual Review Process**
For critical changes, consider inviting a collaborator for genuine code review:
```bash
# Add temporary collaborator for review
gh api repos/OWNER/REPO/collaborators/USERNAME -X PUT

# Remove after review
gh api repos/OWNER/REPO/collaborators/USERNAME -X DELETE
```

#### AI Agent Validation Checklist

Before using admin override, ensure:
- [ ] **Code Quality**: All formatting and linting checks pass
- [ ] **Tests**: Unit, integration, and E2E tests are successful  
- [ ] **Security**: No hardcoded secrets or security vulnerabilities
- [ ] **Functionality**: Changes work as expected in local environment
- [ ] **Documentation**: Any necessary documentation updates included
- [ ] **Rollback Plan**: Confident in ability to revert changes if needed

This workflow maintains code quality while enabling efficient AI-assisted development in single-developer environments.

## 🔧 Troubleshooting & FAQ

### Common Development Issues

**❌ Problem: Integration CI fails with "PR already exists"**
```
Solution: This is actually expected behavior! Check the existing PR 
from integration → develop for an update comment with your latest CI results.
```

**❌ Problem: Develop deployment successful but no PR to main created**
```bash
# Check if stage deployment actually completed successfully
gh run list --branch develop --limit 3

# Look for develop-cd.yml workflow failure
gh run view [RUN_ID] --log

# Common causes:
# 1. Stage deployment failed (check Docker build/push steps)
# 2. PAT_FOR_CI secret missing or expired
# 3. Pre-release creation failed
# 4. No changes since last main merge
```

**❌ Problem: Production PR created but missing deployment information**
```
Solution: Check the develop-cd.yml workflow logs. The PR creation
step runs after successful stage deployment, so missing info usually
indicates a workflow step failure during RC version generation or
Docker image creation.
```

**❌ Problem: "GitHub Actions is not permitted to create pull requests"**
```bash
# Error message in workflow logs
pull request create failed: GraphQL: GitHub Actions is not permitted to create 
or approve pull requests (createPullRequest)

# Solution: Repository owner needs to set up PAT_FOR_CI secret
# See "Repository Setup for Auto-PR Creation" section above for detailed steps
```

**❌ Problem: Auto-PR not created after pushing to integration**
```bash
# Check CI status
gh run list --branch integration --limit 5

# View specific workflow run
gh run view [RUN_ID] --log

# Common causes:
# 1. CI tests failed (fix tests first)
# 2. No changes since last develop merge
# 3. Workflow permissions issue (check repo settings)
```

**❌ Problem: Local tests pass but CI fails**
```bash
# Ensure consistent environment
docker compose -f docker-compose.local.yml down
docker compose -f docker-compose.local.yml up -d

# Run tests exactly like CI
mvn clean test -Dspring.profiles.active=local
mvn failsafe:integration-test failsafe:verify -Dtest=none -DfailIfNoTests=false
```

**❌ Problem: Spotless formatting failures**
```bash
# Fix formatting automatically
mvn spotless:apply

# Check what needs formatting
mvn spotless:check
```

**❌ Problem: "Push to develop/main rejected"**
```bash
# Error message when trying to push directly
remote: error: GH006: Protected branch update failed for refs/heads/develop
remote: error: Cannot push to this branch

# Solution: This is intentional security! Use PR workflow instead
git checkout -b feature/my-changes
git push origin feature/my-changes
# Then create PR: feature/my-changes → develop (or integration)
```

**❌ Problem: "Need to create PR but can't remember workflow"**
```bash
# Quick reference for secure workflow:
# 1. Push to integration (admin direct push allowed)
git push origin integration

# 2. Review auto-created PR: integration → develop  
gh pr list --head integration --base develop

# 3. Use admin override to merge after validation
gh pr merge [PR_NUMBER] --admin --merge

# 4. Review auto-created PR: develop → main
gh pr list --head develop --base main
```

### Environment Problems

**Database Connection Issues:**
```bash
# Check if PostgreSQL container is running
docker ps | grep postgres

# Reset database
docker compose -f docker-compose.local.yml down
docker volume rm product-catalog_postgres-data-local
docker compose -f docker-compose.local.yml up -d
```

**Port Conflicts:**
```bash
# Check what's using port 8080
lsof -i :8080

# Use different port
docker compose -f docker-compose.local.yml up -d
# Edit docker-compose.local.yml ports section if needed
```

### CI/CD Debugging

**Workflow Not Triggering:**
```bash
# Check workflow syntax
gh workflow list

# Manually trigger workflow (if applicable)
gh workflow run "Integration Branch CI"

# Check workflow file syntax locally
yamllint .github/workflows/integration-cd.yml
```

**Auto-PR Missing Information:**
```bash
# The workflow uses these git commands:
git log -1 --pretty=format:'%s'          # Latest commit message
git rev-list --count develop..integration # Commit count

# Test locally to see what the workflow will use
```

### Performance Issues

**Slow Tests:**
```bash
# Run only specific test classes
mvn test -Dtest=ProductServiceTest

# Skip integration tests for faster feedback
mvn test -DskipITs=true

# Parallel test execution
mvn test -Dtest.parallel=true
```

**Memory Issues:**
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=256m"

# Check Docker resources
docker system df
docker system prune
```

### Getting Help

**Resources:**
- 📖 **Documentation**: Check other docs in `/docs/` folder
- 🤖 **GitHub Actions**: View workflow runs for detailed logs
- 🐛 **Issues**: Create GitHub issue for bugs or questions
- 💬 **Discussions**: Use GitHub Discussions for general questions

**Debugging Checklist:**
1. ✅ Check GitHub Actions logs
2. ✅ Verify branch protection rules
3. ✅ Confirm environment variables
4. ✅ Test locally first
5. ✅ Review recent changes
6. ✅ Check for conflicting PRs

---

## 🎯 Quick Reference

**Essential Commands:**
```bash
# Start development
docker compose -f docker-compose.local.yml up -d

# Run all tests
mvn verify

# Format code
mvn spotless:apply

# Check CI status
gh run list --branch integration

# Check auto-PR status (integration → develop)
gh pr list --head integration --base develop

# Check auto-PR status (develop → main)
gh pr list --head develop --base main

# Monitor complete automation chain
gh pr list --state open  # Shows all active PRs

# Create feature branch
git checkout integration && git pull && git checkout -b feature/my-feature

# Complete automated workflow chain:
git push origin integration  # 1. Triggers integration CI → auto-PR to develop
# (Review & merge PR) → 2. Triggers stage deployment → auto-PR to main
# (Review & merge PR) → 3. Triggers production deployment
```

**Important URLs:**
- 🌐 Local API: http://localhost:8080
- 📊 Swagger UI: http://localhost:8080/swagger-ui/index.html
- ❤️ Health Check: http://localhost:8080/actuator/health
- 🔍 GitHub Actions: https://github.com/The-Dave-Stack/product-catalog/actions

This development guide is your companion for contributing effectively to the Product Catalog project. When in doubt, refer back to these guidelines and don't hesitate to ask questions!