param([string]$JdkPath=$env:JAVA_HOME)
$ErrorActionPreference='Stop'
if(!$JdkPath){throw 'Provide -JdkPath or set JAVA_HOME to an installed JDK'}
$project=Split-Path $PSScriptRoot -Parent
$fixture=Join-Path ([IO.Path]::GetTempPath()) ('cscalc-checks-'+[Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $fixture | Out-Null
try {
    & (Join-Path $JdkPath 'bin/javac.exe') -encoding UTF-8 --release 8 -d $fixture (Join-Path $project 'app/src/main/java/com/dbuster/cscalculator/GradeCalculator.java') (Join-Path $PSScriptRoot 'GradeCalculatorChecks.java')
    if($LASTEXITCODE -ne 0){throw 'Compilation failed'}
    & (Join-Path $JdkPath 'bin/java.exe') -cp $fixture GradeCalculatorChecks
    if($LASTEXITCODE -ne 0){throw 'Checks failed'}
} finally {
    $resolved=[IO.Path]::GetFullPath($fixture)
    if(!$resolved.StartsWith([IO.Path]::GetFullPath([IO.Path]::GetTempPath()),[StringComparison]::OrdinalIgnoreCase)){throw 'Unsafe cleanup path'}
    Remove-Item -LiteralPath $resolved -Recurse -Force
}
