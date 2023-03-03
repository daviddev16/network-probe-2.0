# 📪 network-probre-2.0 (Proof of Concept stage)

#### Network service for managing connection to database routes (Client & Server side)

[![Java CI with Maven](https://github.com/daviddev16/network-probe-2.0/actions/workflows/maven.yml/badge.svg)](https://github.com/daviddev16/network-probe-2.0/actions/workflows/maven.yml)

<br>

The main purpose is to integrate network-probe with any kind of enterprise system that uses on-premise or cloud database connections, making it easier to configure from a user's point of view, not worrying with database names, DNS problems, configuration IP changing over time. Network-Probe can help users that are not advanced to easily use what they need.

This version is more generic and flexible, you can configure your own routes and rules to the server, once it is all configured, users can use the system with the client-side implemented on their system, it will "find" the route configuration of the network and the client will no longer need to worry about the real configuration. 

### 💠 How it works?

Firstly, the server service needs to be installed on any machine in the network (Ex.: 192.168.1.0/24), once it is initialized, the client program on any machine in the same network will send a broadcast with 3 attempts waiting for the server response with a `HELLO` packet, when the server service responds to the client broadcast, the client program will start an TCP connection to transmit all the information needed to load the system.  

![Client and server log](https://i.imgur.com/wC7yRKX_d.webp?maxwidth=1920)
This version is not committed yet!

## Usage CLI

Client side example:
```
java -jar "network-probe-1.0-SNAPSHOT-jar-with-dependencies.jar" --load-as=client --bind=192.168.1.8 --verbose
```

Server side example:
```
java -jar "network-probe-1.0-SNAPSHOT-jar-with-dependencies.jar" --load-as=server --bind=0.0.0.0 --verbose
```

## JRE

This project is being developed on "Temurin JDK 8" for compatibility reasons.
