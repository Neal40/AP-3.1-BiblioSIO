package rudy_yoann.backend.Emprunt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EmpruntEmbeddedJSONSerializer extends JsonSerializer<Emprunt> {
    @Override
    public void serialize(Emprunt value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("statut", String.valueOf(value.getStatut()));
        gen.writeStringField("url", "/emprunteurs/" + value.getId());
        gen.writeEndObject();
    }
}
