package me.mouamle.sync.packet;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class Packet implements Serializable {

    private String type;

    @NotNull
    private Object payload;

    public Packet() { }

    public Packet(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return Objects.equals(type, packet.type) &&
                Objects.equals(payload, packet.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, payload);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }

}
