package me.mouamle.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import me.mouamle.sync.event.*;
import me.mouamle.sync.packet.Packet;
import me.mouamle.sync.packet.handler.Handler;
import org.greenrobot.eventbus.EventBus;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SyncServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(SyncServer.class);

    private final Gson gson;
    private final Validator validator;
    private final EventBus eventBus;

    public SyncServer(InetSocketAddress address) {
        super(address);
        this.gson = new GsonBuilder().serializeNulls().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .logSubscriberExceptions(true)
                .build();
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        this.onMessage(conn, new String(message.array(), StandardCharsets.US_ASCII));
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onMessage(WebSocket socket, String message) {
        try {
            final Packet packet = gson.fromJson(message, Packet.class);
            final Optional<Class> oPayloadClass = Registry.getPayloadClass(packet.getType());
            if (!oPayloadClass.isPresent()) {
                logger.warn("Received an unregistered packet type ({})", packet.getType());
                publishEvent(new OnMessage(socket, message, false, "packet type error"));
                return;
            }

            final Set<ConstraintViolation<Packet>> packetViolations = validator.validate(packet);
            if (!packetViolations.isEmpty()) {
                logger.error("Failed to validate incoming packet");
                packetViolations.forEach(violation -> logger.error(violation.getMessage()));
                publishEvent(new OnMessage(socket, message, false, "packet validation error"));
                return;
            }

            final Class payloadClass = oPayloadClass.get();

            final Optional<Handler> oHandler = Registry.getHandler(payloadClass);
            if (!oHandler.isPresent()) {
                logger.warn("Could not find handler for packet type ({})", packet.getType());
                publishEvent(new OnMessage(socket, message, false, "unregistered handler"));
                return;
            }

            final String parsedPayload = gson.toJson(packet.getPayload());
            final Object payload = gson.fromJson(parsedPayload, payloadClass);

            final Set<ConstraintViolation<Object>> payloadViolations = validator.validate(payload);
            if (!payloadViolations.isEmpty()) {
                logger.error("Failed to validate payload for packet type ({})", packet.getType());
                payloadViolations.forEach(violation -> {
                    final String name = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
                    logger.error("{} {}", name, violation.getMessage());
                });

                final List<String> messages = payloadViolations.stream()
                        .map(violation -> String.format("(%s) %s",
                                ((PathImpl) violation.getPropertyPath()).getLeafNode().getName(),
                                violation.getMessage()
                        ))
                        .collect(Collectors.toList());
                socket.send(gson.toJson(new Packet("payload-validation-errors", messages)));
                publishEvent(new OnMessage(socket, message, false, "payload validation error"));
                return;
            }

            oHandler.get().handle(socket, payload);
            publishEvent(new OnMessage(socket, message, true, ""));
        } catch (JsonSyntaxException e) {
            logger.error("incorrect json format ", e);
            publishEvent(new OnMessage(socket, message, false, "json format exception"));
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        publishEvent(new OnOpen(conn, handshake));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        publishEvent(new OnClose(conn, code, reason, remote));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        publishEvent(new OnError(conn, ex));
    }

    @Override
    public void onStart() {
        logger.info("Sync Server started");
        publishEvent(new OnStart());
    }

    private void publishEvent(Event event) {
        eventBus.post(event);
    }

}
