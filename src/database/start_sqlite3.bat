rem -header / -column: pretty formatting
rem default: open yourshop.db, else specify: start_sqlite3.bat <db_name> in cmd

@echo off
set arg=%1
if "%arg%" == "" (set arg="yourshop.db")
.\sqlite-tools-win32-x86-3270200\sqlite3.exe -header -init scripts\init_script.sql -column %arg%