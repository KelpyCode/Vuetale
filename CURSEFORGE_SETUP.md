# CurseForge Deployment Setup

The GitHub Actions workflow will automatically upload releases to CurseForge. To enable this, you need to configure two repository secrets.

## Required Repository Secrets

Go to your GitHub repository → **Settings** → **Secrets and variables** → **Actions**, then add:

### 1. CURSEFORGE_API_TOKEN
1. Go to [CurseForge Console](https://console.curseforge.com/)
2. Navigate to **API Keys** in the left sidebar
3. Click **Generate API Key**
4. Copy the generated token and add it as `CURSEFORGE_API_TOKEN`

### 2. CURSEFORGE_PROJECT_ID
1. Go to your project on CurseForge
2. Look at the URL: `https://www.curseforge.com/minecraft/mc-mods/your-project-name`
3. Or check your project's **About** section for the Project ID
4. Add the numeric ID as `CURSEFORGE_PROJECT_ID`

## Workflow Configuration

The workflow is configured to:
- Upload the shadow JAR (`*-all.jar`) 
- Use the git tag version (e.g., `v1.0.9-preview` becomes `1.0.9-preview`)
- Mark releases as "release" type (change to "beta" or "alpha" if needed)
- Target Hytale loader with Java 21
- Support Hytale version 1.0.0 (update as needed)

## Customization

Edit `.github/workflows/release.yml` to modify:
- `version-type`: Change to `beta` or `alpha` for preview releases  
- `game-versions`: Update supported Hytale versions
- `loaders`: Modify if supporting multiple mod loaders
