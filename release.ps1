#!/usr/bin/env pwsh
param(
    [Parameter()]
    [ValidateSet("patch", "minor", "major", "preview")]
    [string]$BumpType = "preview",

    [Parameter()]
    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

function Get-CurrentVersion {
    $buildFile = "build.gradle.kts"
    $content = Get-Content $buildFile -Raw

    if ($content -match 'version = "([^"]+)"') {
        return $matches[1]
    }

    throw "Could not find version in $buildFile"
}

function Update-Version {
    param([string]$NewVersion)

    $buildFile = "build.gradle.kts"
    $content = Get-Content $buildFile -Raw
    $newContent = $content -replace 'version = "[^"]+"', "version = `"$NewVersion`""

    if (-not $DryRun) {
        Set-Content $buildFile $newContent -NoNewline
        Write-Host "✅ Updated version in $buildFile to $NewVersion" -ForegroundColor Green
    } else {
        Write-Host "🔍 Would update version in $buildFile to $NewVersion" -ForegroundColor Yellow
    }
}

function Bump-Version {
    param([string]$CurrentVersion, [string]$BumpType)

    # Parse version like "1.0.8-preview" or "1.0.8"
    if ($CurrentVersion -match '^(\d+)\.(\d+)\.(\d+)(?:-(.+))?$') {
        $major = [int]$matches[1]
        $minor = [int]$matches[2]
        $patch = [int]$matches[3]
        $suffix = $matches[4]

        switch ($BumpType) {
            "major" {
                $major++; $minor = 0; $patch = 0; $suffix = "preview"
            }
            "minor" {
                $minor++; $patch = 0; $suffix = "preview"
            }
            "patch" {
                $patch++; $suffix = "preview"
            }
            "preview" {
                if ($suffix -eq "preview") {
                    $patch++
                } else {
                    $suffix = "preview"
                }
            }
        }

        if ($suffix) {
            return "$major.$minor.$patch-$suffix"
        } else {
            return "$major.$minor.$patch"
        }
    }

    throw "Invalid version format: $CurrentVersion"
}

function Push-Release {
    param([string]$Version)

    $tag = "v$Version"

    if (-not $DryRun) {
        # Check if we're in a git repo
        if (-not (Test-Path ".git")) {
            throw "Not in a git repository"
        }

        # Check for uncommitted changes
        $status = git status --porcelain
        if ($status -and ($status -notmatch "build\.gradle\.kts")) {
            Write-Warning "You have uncommitted changes (other than build.gradle.kts):"
            git status --short
            $continue = Read-Host "Continue anyway? (y/N)"
            if ($continue -ne "y" -and $continue -ne "Y") {
                exit 1
            }
        }

        # Commit version change
        git add build.gradle.kts
        git commit -m "Bump version to $Version"

        # Delete existing tag if it exists
        git tag -d $tag 2>$null
        git push origin ":refs/tags/$tag" 2>$null

        # Create and push new tag
        git tag $tag
        git push origin $tag
        git push origin main

        Write-Host "🚀 Released $tag - check GitHub Actions for build progress" -ForegroundColor Green
        Write-Host "   https://github.com/$(git remote get-url origin | ForEach-Object { $_ -replace '.*github\.com[:/](.+)\.git.*', '$1' })/actions" -ForegroundColor Cyan
    } else {
        Write-Host "🔍 Would create and push tag: $tag" -ForegroundColor Yellow
        Write-Host "🔍 Would commit build.gradle.kts changes" -ForegroundColor Yellow
    }
}

# Main execution
try {
    $currentVersion = Get-CurrentVersion
    $newVersion = Bump-Version $currentVersion $BumpType

    Write-Host "Current version: $currentVersion" -ForegroundColor Blue
    Write-Host "New version: $newVersion ($BumpType bump)" -ForegroundColor Blue
    Write-Host ""

    Update-Version $newVersion
    Push-Release $newVersion

} catch {
    Write-Error "❌ Release failed: $($_.Exception.Message)"
    exit 1
}
