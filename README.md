#App Cleaner

###Redis


##### Linux
```
sudo apt-get update
sudo apt-get install build-essential tcl
mkdir redis && cd redis
wget https://download.redis.io/releases/redis-6.2.1.tar.gz
tar xzf redis-6.2.1.tar.gz
cd redis-6.2.1/
make
make cleaner

sudo service redis-server restart
redis-server
src/redis-cli
redis-cli ping

redis-cli info
redis-cli info stats
redis-cli info server
```

##### WSL

```
sudo apt update && apt upgrade
sudo apt install redis-server
sudo nano /etc/redis/redis.conf
sudo service redis-server start
redis-server
redis-cli
sudo service redis-server restart
```
