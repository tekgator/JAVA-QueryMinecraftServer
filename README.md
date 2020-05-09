# QueryMinecraftServer

## Overview

QueryMinecraftServer is a library to access Minecraft Server information with ease.
It can be used to query MC server without activating the query option in the MC 
server properties. 

This is achieved by utilizing the [Server List Ping (SLP)](https://wiki.vg/Server_List_Ping#Ping_Process) protocol base on TCP.
If even more information like to be queried e.g. the installed mods, etc. this can 
also easily be done by enabling the query mode of the MC server. The library will then 
utilize the [UT3 (or GameSpot) Query Protocol](https://wiki.vg/Query)

## Features
- Easy to use with just one line of code
- Support for newest Minecraft server version
- **NO** requirement for plugin, RCON or to enable Query on the server, it works straight out of the box on every Minecraft server in TCP mode.
- Retrieves online player names
- [SRV record lookup](https://www.namecheap.com/support/knowledgebase/article.aspx/9765/2208/how-can-i-link-my-domain-name-to-a-minecraft-server): No need to know the exact port of the Minecraft server if a SRV record is configured

## Installing

For right now you can download the JAR in the [release](https://github.com/tekgator/JAVA-QueryMinecraftServer/releases) section or include the dependency into your project via [Jitpack.io](https://jitpack.io/#tekgator/JAVA-QueryMinecraftServer):

##### Maven
```xml
<dependency>
  <groupId>com.github.tekgator</groupId>
  <artifactId>JAVA-QueryMinecraftServer</artifactId>
  <version>1.1</version>
</dependency>
```
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

##### Gradle
```gradle
dependencies {
    implementation 'com.github.tekgator:JAVA-QueryMinecraftServer:1.1'
}

repositories {
    maven { url 'https://jitpack.io' }
}
```

## How to use it

The QueryStatus object is created by it's builder class, the only mandatory parameter 
is the host name.
If UDP is used please **NOTE** that [UDP query](https://wiki.vg/Query) needs to be activated on the MC server.

##### Mandatory parameter:
- Hostname or IP address of the MC server

##### Optional parameter:
- *Port:* if not provided the library is doing a SRV record lookup]. If still nothing is found the default MC server port 25565 is used
- *Protocol:*
  - *TCP:* This is the default protocol to query the MC server and will work with any version >= 1.7
  - *TCP_DEPRECIATED:* Same as above for MC server version <= 1.6, less information will be provided
  - *UDP_FULL:* Provides most information about a the server e.g. including a mod list, but needs UDP query activated
  - *UDP_BASIC:* Same as above but with less information
- *Timeout:* Default is a 1000ms timeout for the conenction. If it happens to be a slow MC server this might need to be increased.

### Coding

```Java
import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryStatus;
import com.tekgator.queryminecraftserver.api.Status;
.
.
.
System.out.println(new QueryStatus.Builder("my-mcserver.com")
                                        .setProtocol(Protocol.TCP)
                                        .build()
                                        .getStatus()
                                        .toJson());
```


### Properties to retrieve server information (Status object)

- *HostName*: Hostname of the server (maybe different from input due to SRV record resolving)
- *IPAdress*: IP adress of the server
- *Port*: Port of the server (maybe gathered from the SRV record)
- *Latency*: Ping to the server im ms
- *FavIcon*: Server icon as base64 string which can be assigned to the src property of the HTML img tag
- *Version*: Minecraft server version
- *Protocol*: Protocol version the Minecraft server is using for communication
- *Description*: Message of the day
- *PlayerMax*: Count of maximum possible players on the server
- *PlayerOnline*: Count of current players on the server
- *Players*: Array of players on the server with name and UUID (for TCP: only a sample is returned from the server, most likly around 10 - 15 players) 
- *ModInfo*: In case of a Spigot / Forge server a mod list maybe returned

Via the ```toJson() ``` all the server information can also be exported to a JSON string.


## Dependencies:
This project requires Java 8+.
All dependencies are managed automatically by Maven.

- Junit
  - Version: 4.11
  - [Github](https://github.com/junit-team/junit5/)
  - [Maven central](https://search.maven.org/artifact/junit/junit/4.11/jar)

- dnsjava
  - Version: 3.0.2
  - [Github](https://github.com/dnsjava/dnsjava)
  - [Maven central](https://search.maven.org/artifact/dnsjava/dnsjava/3.0.2/bundle)

- Gson
  - Version: 2.8.6
  - [Github](https://github.com/google/gson)
  - [Maven central](https://search.maven.org/artifact/com.google.code.gson/gson/2.8.6/jar)
