
set HOSTIP=%1
set PORTADDRESS=%2
set DBNAME=%3
set SUPERUSER=%4
set PGPASSWORD=%5

set SCHEMANAME=egoverp

pg_restore --host %HOSTIP% --port %PORTADDRESS% --username "%SUPERUSER%" --dbname "%DBNAME%" --schema %SCHEMANAME% ".\dumps\egoverp.dump"

