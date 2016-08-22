pid=` jps -l | grep  'jaserver' | awk '{print $1}'`
echo "kill pid : $pid"
kill -9 $pid