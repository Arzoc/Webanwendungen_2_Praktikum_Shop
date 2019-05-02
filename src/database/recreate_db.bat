REM recreates db with automatic insertion of sample values in scripts/insert_sample_values.sql

@echo off
set arg=%1
if "%arg%" == "" (set arg="yourshop.db")
del %arg%
.\sqlite-tools-win32-x86-3270200\sqlite3.exe -header -column %arg% ".read scripts\\recreate_db.sql"