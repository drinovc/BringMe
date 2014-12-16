@ECHO OFF
echo Open site: http://localhost:8080/
chrome "http://localhost:8080/"
sencha fs web -p 8080 start -map .