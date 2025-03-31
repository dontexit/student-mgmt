@echo off
if "%MODULEPATH%"=="" (
    echo MODULEPATH is not set, set MODULEPATH before running the script again.
    exit /b
)

javac --module-path "%MODULEPATH%" --add-modules javafx.controls -d bin src\StudentAdmin.java
java --module-path "%MODULEPATH%" --add-modules javafx.controls -cp bin StudentAdmin
