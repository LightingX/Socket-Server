#Description
This is a demo project written a half and a year ago, which help me understand the socket API provided by Java.

I 'd like to optimize this demo using NIO and JUC I've learned recently.
#Design
This socket server now has a main thread to listen the connecting requests sent by clients. After receiving the request, the server start a new thread to handle the connection.

When receiving a message sent by a client, the server will forward the message to other clients.

When receiving a message *disconnect*, the corresponding client handler will be terminated. Certainly, the socket connection will also be closed.
#How To Use
This demo project is very simple, you should only install the JDK(1.8+) package, then use the following commands:
> javac Server.java
> 
> javac Client.java

Then the java source file will be compile to Java byte code, which can be recognized by Java Virtual Machine.

You can run the server program by this command:
> java Server

You can run a client by this command:
> java Client

This command can be run many times, and there will be many clients connect to a server.