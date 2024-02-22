package rudy_yoann.backend.Article;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import rudy_yoann.backend.Emprunt.Emprunt;

import java.io.IOException;

public class ArticleEmbeddedJSONSerializer extends JsonSerializer<Article> {
    @Override
    public void serialize(Article value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("titre", String.valueOf(value.getTitre()));
        gen.writeStringField("url", "/articles/" + value.getId());
        gen.writeEndObject();
    }
}
