@ECHO OFF
echo Open site: http://localhost:8888/
chrome "http://localhost:8888/"
sencha fs web -p 8888 start -map .