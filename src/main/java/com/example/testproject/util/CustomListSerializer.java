package com.example.testproject.util;

import com.example.testproject.models.Counterpart;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListSerializer extends StdSerializer<List<Counterpart>> {
    public CustomListSerializer() {
        this(null);
    }

    public CustomListSerializer(Class<List<Counterpart>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Counterpart> counterparts,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Integer> ids = new ArrayList<>();
        for (Counterpart counterpart : counterparts) {
            ids.add(counterpart.getId());
        }
        generator.writeObject(ids);
    }
}
