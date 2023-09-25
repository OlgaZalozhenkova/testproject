package com.example.testproject.util;

import com.example.testproject.models.Supplier;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListSerializer extends StdSerializer<List<Supplier>> {
    public CustomListSerializer() {
        this(null);
    }

    public CustomListSerializer(Class<List<Supplier>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Supplier> suppliers,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Integer> ids = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            ids.add(supplier.getId());
        }
        generator.writeObject(ids);
    }
}
