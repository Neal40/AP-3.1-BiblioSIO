package rudy_yoann.backend.DemandeEmprunt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import rudy_yoann.backend.Emprunt.Emprunt;

import java.io.IOException;

public class DemandeEmpruntEmbeddedJSONSerializer extends JsonSerializer<DemandeEmprunt> {
    @Override
    public void serialize(DemandeEmprunt value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("statut", String.valueOf(value.getStatut()));
        gen.writeStringField("url", "/demandeEmprunts/" + value.getId());
        gen.writeEndObject();
    }
}
