#!/usr/bin/env pwsh
param(
    [Parameter(Mandatory=$true)]
    [string]$Material
)

# Validate input
if ([string]::IsNullOrWhiteSpace($Material)) {
    Write-Host "Error: Material parameter cannot be empty" -ForegroundColor Red
    exit 1
}

# Convert material to lowercase for consistent naming
$materialLower = $Material.ToLower()
$materialCapitalized = (Get-Culture).TextInfo.ToTitleCase($materialLower)

Write-Host "Creating girder files for material: $materialCapitalized" -ForegroundColor Green

# Define paths
$sourceJavaDir = "src\main\java\com\agent772\createmoregirder\content\brass_girder"
$targetJavaDir = "src\main\java\com\agent772\createmoregirder\content\${materialLower}_girder"
$sourceModelDir = "src\main\resources\assets\createmoregirder\models\block\brass_girder"
$targetModelDir = "src\main\resources\assets\createmoregirder\models\block\${materialLower}_girder"
$sourceEncasedModelDir = "src\main\resources\assets\createmoregirder\models\block\brass_girder_encased_shaft"
$targetEncasedModelDir = "src\main\resources\assets\createmoregirder\models\block\${materialLower}_girder_encased_shaft"
$projectRoot = $PSScriptRoot

# Change to project directory
Push-Location $projectRoot

try {
    # Check if source directories exist
    if (!(Test-Path $sourceJavaDir)) {
        Write-Host "Error: Source Java directory '$sourceJavaDir' not found" -ForegroundColor Red
        exit 1
    }
    
    if (!(Test-Path $sourceModelDir)) {
        Write-Host "Error: Source model directory '$sourceModelDir' not found" -ForegroundColor Red
        exit 1
    }

    # Create target directories if they don't exist
    $dirsToCreate = @($targetJavaDir, $targetModelDir, $targetEncasedModelDir)
    foreach ($dir in $dirsToCreate) {
        if (!(Test-Path $dir)) {
            New-Item -ItemType Directory -Path $dir -Force | Out-Null
            Write-Host "Created directory: $dir" -ForegroundColor Yellow
        } else {
            Write-Host "Warning: Directory '$dir' already exists. Files will be overwritten." -ForegroundColor Yellow
        }
    }
    
    # Ask for confirmation if any directories exist
    $existingDirs = $dirsToCreate | Where-Object { Test-Path $_ }
    if ($existingDirs.Count -gt 0) {
        $response = Read-Host "Continue and overwrite existing files? (y/N)"
        if ($response -ne 'y' -and $response -ne 'Y') {
            Write-Host "Operation cancelled" -ForegroundColor Yellow
            exit 0
        }
    }

    # Process Java files
    $javaFiles = Get-ChildItem -Path $sourceJavaDir -Filter "*.java"
    Write-Host "Found $($javaFiles.Count) Java files to process:" -ForegroundColor Cyan
    foreach ($file in $javaFiles) {
        Write-Host "  - $($file.Name)" -ForegroundColor Gray
    }

    foreach ($sourceFile in $javaFiles) {
        $targetFileName = $sourceFile.Name -replace "Brass", $materialCapitalized
        $targetFilePath = Join-Path $targetJavaDir $targetFileName
        
        Write-Host "Processing Java: $($sourceFile.Name) -> $targetFileName" -ForegroundColor Cyan
        
        $content = Get-Content -Path $sourceFile.FullName -Raw -Encoding UTF8
        $content = $content -replace "brass_girder", "${materialLower}_girder"
        $content = $content -replace "BrassGirder", "${materialCapitalized}Girder"
        $content = $content -replace "BRASS_GIRDER", "${materialLower}_GIRDER".ToUpper()
        $content = $content -replace "brass", $materialLower
        $content = $content -replace "Brass", $materialCapitalized
        $content = $content -replace "BRASS", $materialLower.ToUpper()
        
        # Use UTF8NoBOM to avoid BOM issues
        [System.IO.File]::WriteAllText($targetFilePath, $content, [System.Text.UTF8Encoding]::new($false))
        Write-Host "  Created: $targetFilePath" -ForegroundColor Green
    }

    # Process model files
    $modelDirs = @(
        @{ Source = $sourceModelDir; Target = $targetModelDir; Name = "girder models" },
        @{ Source = $sourceEncasedModelDir; Target = $targetEncasedModelDir; Name = "encased shaft models" }
    )
    
    foreach ($modelDir in $modelDirs) {
        if (Test-Path $modelDir.Source) {
            $modelFiles = Get-ChildItem -Path $modelDir.Source -Filter "*.json"
            Write-Host "Found $($modelFiles.Count) $($modelDir.Name) to process:" -ForegroundColor Cyan
            
            foreach ($modelFile in $modelFiles) {
                Write-Host "  - $($modelFile.Name)" -ForegroundColor Gray
            }

            foreach ($sourceFile in $modelFiles) {
                $targetFilePath = Join-Path $modelDir.Target $sourceFile.Name
                
                Write-Host "Processing Model: $($sourceFile.Name)" -ForegroundColor Cyan
                
                $content = Get-Content -Path $sourceFile.FullName -Raw -Encoding UTF8
                $content = $content -replace "brass_girder", "${materialLower}_girder"
                $content = $content -replace "BrassGirder", "${materialCapitalized}Girder"
                $content = $content -replace "BRASS_GIRDER", "${materialLower}_GIRDER".ToUpper()
                $content = $content -replace "brass", $materialLower
                $content = $content -replace "Brass", $materialCapitalized
                $content = $content -replace "BRASS", $materialLower.ToUpper()
                
                # Use UTF8NoBOM to avoid BOM issues
                [System.IO.File]::WriteAllText($targetFilePath, $content, [System.Text.UTF8Encoding]::new($false))
                Write-Host "  Created: $targetFilePath" -ForegroundColor Green
            }
        }
    }

    $totalJavaFiles = $javaFiles.Count
    $totalModelFiles = 0
    foreach ($modelDir in $modelDirs) {
        if (Test-Path $modelDir.Source) {
            $totalModelFiles += (Get-ChildItem -Path $modelDir.Source -Filter "*.json").Count
        }
    }

    Write-Host "`nSuccess! Created files for $materialCapitalized girder:" -ForegroundColor Green
    Write-Host "  - $totalJavaFiles Java class files" -ForegroundColor Yellow
    Write-Host "  - $totalModelFiles JSON model files" -ForegroundColor Yellow
    
    Write-Host "`nFiles created in:" -ForegroundColor Yellow
    Write-Host "  - Java classes: $targetJavaDir" -ForegroundColor Gray
    Write-Host "  - Models: $targetModelDir" -ForegroundColor Gray
    Write-Host "  - Encased shaft models: $targetEncasedModelDir" -ForegroundColor Gray
    
    Write-Host "`nNext steps:" -ForegroundColor Cyan
    Write-Host "1. Add the new blocks to CMGBlocks.java" -ForegroundColor Gray
    Write-Host "2. Add block entity types to CMGBlockEntityTypes.java" -ForegroundColor Gray
    Write-Host "3. Copy and rename texture files (brass_girder.png -> ${materialLower}_girder.png)" -ForegroundColor Gray
    Write-Host "4. Run data generation: .\gradlew runData" -ForegroundColor Gray

} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
} finally {
    Pop-Location
}