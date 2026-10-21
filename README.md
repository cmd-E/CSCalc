# CSCalc
Калькулятор для вычисления финального балла и предположения финального балла исходя из балла за экзамен


Расчеты и проверка входных значений находятся в GradeCalculator и не зависят от Android.
Десятичные оценки сохраняются; недостижимая цель возвращается отдельным результатом.

Для проверки формул без Android SDK выполните checks/run-checks.ps1 -JdkPath <путь к JDK>.
Android/JUnit проверки находятся в app/src/test. Сборка приложения по-прежнему требует Android SDK.

## Build, tests and CI

The build uses JDK 17, Gradle 8.13, Android Gradle Plugin 8.13.2, Android SDK platform
35 and build tools 35.0.0. The application still targets API 29 and supports API 22+.
Dependencies resolve from Google Maven and Maven Central. The wrapper distribution
and wrapper JAR were verified against Gradle's published SHA-256 checksums.

```sh
./gradlew :app:assembleDebug :app:lintDebug :app:coreCoverageVerification
```

On Windows use `gradlew.bat`. JUnit tests use independent values and require neither
a device nor network calls. They cover decimal parsing, null/blank/malformed values,
non-finite and range checks, both calculation arguments, exact/already-met/impossible
targets, and 441 forward/inverse formula combinations. The GradeCalculator core has
a 100% line and branch coverage gate; Android views are compiled and linted, outside
that core gate. Real-device UI behavior is not covered by these JVM tests.

GitHub Actions builds the debug APK, runs lint and JUnit, enforces the coverage gate
and uploads the APK, test, lint and JaCoCo reports. CodeQL scans Java. Dependabot
checks Gradle and Actions weekly. `.github/branch-ruleset.json` is a proposed ruleset
requiring both jobs; it must be applied separately in repository settings.
