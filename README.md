## Learn Language Speaking Online

### Redis

> redis-server

Linux

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

> WSL

```
sudo apt update && apt upgrade
sudo apt install redis-server
sudo nano /etc/redis/redis.conf
sudo service redis-server start
redis-server
redis-cli
sudo service redis-server restart
```

```FLUSHDB``` – Deletes all keys from the connection's current database.

```FLUSHALL``` – Deletes all keys from all databases.

For example, in your shell:

```
redis-cli flushall
```

### Discovery service Eureka
Hosts eureka server.

PORT: 8761

### Gateway service Zuul
Gateway for microservies. It includes JWT and Rolebased Access.
``
PORT: 8762

### Auth service
Custom Service that provides feature authentication.

PORT: 8871

Swagger:

http://localhost:8762/api/auth/swagger-ui.htm

http://localhost:8762/api/auth/v2/api-docs

### User service
Custom Service that provides feature user.

PORT: 8872

Swagger:

http://localhost:8762/api/user/swagger-ui.htm

http://localhost:8762/api/user/v2/api-docs

### Storage service
Custom Service that provides feature files.

PORT: 8873

Swagger:

http://localhost:8762/api/storage/swagger-ui.htm

http://localhost:8762/api/storage/v2/api-docs

### P2p service
Custom Service that provides feature WebRtc p2p.

PORT: 8874

Swagger:

http://localhost:8762/api/p2p/swagger-ui.htm

http://localhost:8762/api/p2p/v2/api-docs

* P2pController provides HTTP requests handling, model processing and view presentation;
* Domain package includes domain model and service;
* Web Socket based Web RTC Signalling Server is located under the Socket directory;

##### API
Method |      URI           |  Description
 ------ | --------------------------------------------------- | ------- 
 Get | "", "/", "/index", "/home", "/main" | main page application web entry point
 Post | "/room" | process room selection form
 Get | "/room/{sid}/user/{uuid}" | select a room to enter; sid - room number, uuid - user id
 Get | "/room/{sid}/user/{uuid}/exit" | a room exit point for the user selected
 Get | "/room/random" | generates random room number
 Get | "/offer" | demonstrates sample SDP offer
 Get | "/stream" | demonstrates streaming video resolution selection
