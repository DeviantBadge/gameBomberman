package ru.atom.chat.socket.message.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.chat.socket.topics.ReplicaType;

public class OutgoingReplica {
    private final ReplicaType type;
    private final String data;


    public OutgoingReplica(ReplicaType type, String data) {
        this.type = type;
        this.data = data;
    }

    @JsonCreator
    public OutgoingReplica(@JsonProperty("topic") ReplicaType type, @JsonProperty("data") JsonNode data) {
        this.type = type;
        this.data = data.toString();
    }

    public String getData() {
        return data;
    }

    public ReplicaType getTopic() {
        return type;
    }

    @Override
    public String toString() {
        return "{topic:" + type + ",data:" + data + "}";
    }
}
