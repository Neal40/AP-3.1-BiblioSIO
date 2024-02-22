package rudy_yoann.backend.Emprunteur;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EmprunteurEmbeddedJSONSerializer extends JsonSerializer<Emprunteur> {
        @Override
    public void serialize(Emprunteur value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
            gen.writeStringField("nom", value.getNom());
            gen.writeStringField("url", "/emprunteurs/" + value.getId());
        gen.writeEndObject();
    }
}
