@echo off
color E

:CHECK
ECHO Firefox wird gestoppt
tasklist /fi "Imagename eq firefox.exe"
IF ERRORLEVEL 0 GOTO CLEAN
IF ERRORLEVEL 1 GOTO START

:CLEAN
ECHO Firefox Profil wird bereinigt
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
set DataDir=C:\Users\user\AppData\Local\Mozilla\Firefox\Profiles
del /q /s /f "%DataDir%"
rd /s /q "%DataDir%"
for /d %%x in (C:\Users\user\AppData\Roaming\Mozilla\Firefox\Profiles\*) do del /q /s /f %%x\*sqlite
ipconfig /flushdns
GOTO START

:START
ECHO Firefox wird gestartet
start firefox.exe "https://www.ebesucher.de/surfbar/mallex2000.minipc1"
rem start firefox.exe "https://www.ebesucher.de/surfbar/mallex2000.minipc3"
GOTO WAIT

:WAIT
ECHO und wird innerhalb von x - x Stunden neu gestartet
SET MIN=7200
SET MAX=14800
SET /a Z=%MIN%+(%MAX%-%MIN%+1)*%random%/32768
timeout /T %z%
GOTO TIMER

:TIMER
ECHO Firefox wird gestoppt
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
taskkill /im "firefox.exe" /F
GOTO CHECK