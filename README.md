# kotlin-spring-boot
Server res api use kotlin spring boot

#Redis
sudo apt-get update
sudo apt-get install build-essential tcl
mkdir redis && cd redis
wget https://download.redis.io/releases/redis-6.2.1.tar.gz
tar xzf redis-6.2.1.tar.gz
cd redis-6.2.1/
make
make socail

sudo service redis-server restart
redis-server
src/redis-cli
redis-cli ping

redis-cli info
redis-cli info stats
redis-cli info server

#MongoDB
mongo
use admin

```
db.createUser(
 {
 user: "vnapnic",
 pwd: "123456",
 roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
 }
 )
```

show users
quit()


use vnapnic
db
show dbs