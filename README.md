# WSS Handler
[![](https://jitpack.io/v/MouamleH/wss-handler.svg)](https://jitpack.io/#MouamleH/wss-handler)

A library to make working with websockets in java easier.
<br>
Built on top of [TooTallNate's Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)

## Usage Example
First you need to create and register a handler
```java
public class PingHandler extends Handler<String, String> {
    
    @Override
    public void handle(WebSocket socket, String payload) {
        reply(socket, "pong! " + payload);
    }

    @Override
    public String getPacketType() {
        return "ping";
    }

    @Override
    public Class<String> getPayloadType() {
        return String.class;
    }

    @Override
    public Class<String> getResponseType() {
        return String.class;
    }
    
}
```

Registering the handler
```java
Registry.register(new PingHandler());
```

Then you create the server, attach it to a thread and start the thread
```java
int port = 25565;
Server server = new Server(new InetSocketAddress(port));
Thread serverThread = new Thread(server);
serverThread.start()
```

After that when you web socket receives the following json format it will handle it according to the registered handlers
```json5
{
  "type": "ping", // the type to be matched against Handler#getPacketType
  "payload": "Hello" // the payload according to Handler#getPayloadType and will be sent to Handler#handle
}
```

## Events 
The library fires several events based on the server state.
<br>
The event system uses [greenrobot's EventBus](https://github.com/greenrobot/EventBus) 

Available events are:
- OnOpen
- OnStart
- OnClose
- OnError
- OnMessage

> You don't need to subscribe to the events, but they are there if you need them
