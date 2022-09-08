@echo off
color E

cd %USERPROFILE%
del restart_error.log
del restart.log

:CHECK_EBESUCHER_REWARDS
echo check ebesucher rewards
rem https://stedolan.github.io/jq/ 
rem https://curl.se/windows/
SET EBESUCHER_TOKEN=DmZbyirm1rAyxE34pnDQVAmUpHrRGdJYZoZNGQURCrrpjSJKYm
rem SET surflink=minipc1
SET surflink=%COMPUTERNAME%
SET HOUR=%TIME:~0,2%
SET YEAR=%DATE:~6,4%
SET MONTH=%DATE:~3,2%
SET DAY=%DATE:~0,2%
if %COMPUTERNAME% EQU MINIPC2 SET YEAR=%DATE:~10,4%
if %COMPUTERNAME% EQU MINIPC2 SET MONTH=%DATE:~4,2%
if %COMPUTERNAME% EQU MINIPC2 SET DAY=%DATE:~7,2%

rem SET MYDATE=2022-08-08
SET MYDATE=%YEAR%-%MONTH%-%DAY%
echo TODAY=%MYDATE%
set /A HOUR=%HOUR% - 1
echo HOUR=%HOUR%
curl -s "https://www.ebesucher.de/api/visitor_exchange.json/surflink/mallex2000.%surflink%/earnings_hourly/%MYDATE%?timezone=Europe%%2FBerlin" --user "mallex2000:%EBESUCHER_TOKEN%"   -H 'accept: application/json' |  jq .[\"%HOUR%\"] > __query.tmp
set /p LAST_REWARDS=<__query.tmp
del __query.tmp
echo %surflink% %MYDATE% %TIME% - LAST_REWARDS=%LAST_REWARDS%
echo %surflink% %MYDATE% %TIME% - LAST_REWARDS=%LAST_REWARDS% >> restartHistory.log
if %LAST_REWARDS% gtr 0 GOTO WAIT
GOTO CHECK

:CHECK
ECHO === Firefox wird gestoppt
ECHO === Firefox wird gestoppt >> restart_histories.log
tasklist /fi "Imagename eq firefox.exe" >> restart.log 
IF ERRORLEVEL 0 GOTO CLEAN
IF ERRORLEVEL 1 GOTO START

:CLEAN
ECHO === Firefox Profil wird bereinigt
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
set DataDir=C:\Users\user\AppData\Local\Mozilla\Firefox\Profiles
del /q /s /f "%DataDir%" >> restart.log 2> restart_error.log
rd /s /q "%DataDir%" >> restart.log 2> restart_error.log
for /d %%x in (C:\Users\user\AppData\Roaming\Mozilla\Firefox\Profiles\*) do del /q /s /f %%x\*sqlite >> restart.log 2> restart_error.log
ipconfig /flushdns >> restart.log 
GOTO START

:START
ECHO === Firefox wird gestartet
start firefox.exe "https://www.ebesucher.de/surfbar/mallex2000.minipc1"
rem start firefox.exe "https://www.ebesucher.de/surfbar/mallex2000.minipc3"
GOTO WAIT

:WAIT
SET MIN=3600
SET MAX=10800
SET /a MIN_H=%MIN%/3600
SET /a MAX_H=%MAX%/3600
ECHO === Warte zwischen %MIN_H% und %MAX_H% Stunden
SET /a Z=%MIN%+(%MAX%-%MIN%+1)*%random%/32768
timeout /T %z%
GOTO TIMER

:TIMER
ECHO === Firefox wird gestoppt
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
taskkill /im "firefox.exe" /F >> restart.log 2> restart_error.log
GOTO CHECK