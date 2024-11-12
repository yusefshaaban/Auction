cd server
rmiregistry &
sleep 2
java Server  &
cd ../client
sleep 2
java Client 2
