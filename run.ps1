$ErrorActionPreference = "Stop"

$jar = Get-ChildItem -Path "lib" -Filter "*.jar" | Select-Object -First 1
if (-not $jar) {
    Write-Host "SQLite JDBC jar not found."
    Write-Host "Download sqlite-jdbc from https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/"
    Write-Host "Place it in the lib folder, then run this script again."
    exit 1
}

if (-not (Test-Path "out")) {
    New-Item -ItemType Directory -Path "out" | Out-Null
}

$sources = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java" | ForEach-Object { $_.FullName }
javac -cp "lib/*" -d "out" $sources
java -cp "out;lib/*" com.habittracker.Main
