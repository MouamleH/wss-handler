package me.mouamle.sync;

import me.mouamle.sync.packet.Registry;
import me.mouamle.sync.packet.handler.Handler;
import org.java_websocket.WebSocket;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.net.InetSocketAddress;

public class Application {

    public static class Payload implements Serializable {

        @NotEmpty
        private String name;

        public Payload() { }

        public Payload(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "name='" + name + '\'' +
                    '}';
        }

    }

    public static void main(String[] args) {
        Registry.register(new Handler<Payload>() {

            @Override
            public void handle(WebSocket socket, Payload payload) {
                System.out.println(payload);
            }

            @Override
            public String getPacketName() {
                return "test";
            }

            @Override
            public Class<Payload> getPayloadType() {
                return Payload.class;
            }

        });
        final SyncServer server = new SyncServer(new InetSocketAddress(1999));
        new Thread(server, "sync-server").start();
    }
}
