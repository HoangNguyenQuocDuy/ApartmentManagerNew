package hnqd.aparmentmanager.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EnumSerialize extends JsonSerializer<Enum<?>> {

    @Override
    public void serialize(Enum<?> anEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(anEnum.name().substring(0, 1).toUpperCase() + anEnum.name().substring(1).toLowerCase());
    }
}
