#App Cleaner

# kotlin-spring-boot
Server res api use kotlin spring boot

#Redis
```
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
```

#MongoDB

```
sudo apt-get purge mongodb-org*
sudo apt-get install mongodb

sudo apt-get install gnupg
sudo apt-get update

mongo --version
```

Uninstall and remove any Mongo packages.

```sudo apt-get purge mongodb-org*```

Check if related directories removed

```sudo rm -r /var/log/mongodb```

```sudo rm -r /var/lib/mongodb```

Recheck for autoremove any remaining mongo packages

sudo apt-get autoremove
Configure your directory

```sudo dpkg --configure -a```

Force install anything required

```sudo apt-get install -f```

######Start MongoDB
```
sudo systemctl start mongod
```

```
mongo
use admin

db.createUser(
 {
 user: "vnapnic",
 pwd: "123456",
 roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
 }
 )


show users
quit()


use vnapnic
db
show dbs
```

remove complete mongodb

``` 
sudo apt-get autoremove --purge mongodb
sudo apt-get remove mongodb* --purge 

```

##### MongoDB on WSL

