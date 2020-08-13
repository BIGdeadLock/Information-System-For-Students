set mytime=%date%
echo %mytime%

SET searchString=%date%
SET key=%1

CALL SET keyRemoved=%%searchString:%key%=%%

IF NOT "x%keyRemoved%"=="x%searchString%" (
   java -jar PrivateProject.jar Tue
)