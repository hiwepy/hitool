#ftpclient
=========


FTPClient扩展，实现连接的复用，减少FTP连接的消耗
1、基于ThreadLocal机制实现的FTPClient扩展，实现当前线程的FTPClient对象复用
2、基于commons-net 和 commons-pool2实现的FTPClient扩展；实现FTPClient对象池复用
3、扩展FPTFile文件过滤实现
4、扩展FTPClient工具函数


### Maven Dependency

``` xml
<dependency>
	<groupId>net.jeebiz</groupId>
	<artifactId>jeebiz-ftpclient</artifactId>
	<version>${project.version}</version>
</dependency>
```

### Usage
------------
``` 
```