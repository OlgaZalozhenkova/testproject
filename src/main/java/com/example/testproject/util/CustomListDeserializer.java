package com.example.testproject.util;

import com.example.testproject.models.Supplier;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListDeserializer extends StdDeserializer<List<Supplier>> {

    private static final long serialVersionUID = 1095767961632979804L;

    public CustomListDeserializer() {
        this(null);
    }

    public CustomListDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Supplier> deserialize(final JsonParser jsonparser,
                                      final DeserializationContext context)
            throws IOException, JsonProcessingException {
        return new ArrayList<Supplier>();
    }

}