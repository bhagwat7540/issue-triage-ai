package rag;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EmbeddingService implements EmbeddingServiceInterface{

    private static final String API_URL = "https://api.openai.com/v1/embeddings";
    private static final String MODEL = "text-embedding-3-small";

    private final String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public EmbeddingService(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public float[] embed(String text) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setDoOutput(true);

            String body = mapper.writeValueAsString(
                    new EmbeddingRequest(MODEL, text)
            );

            try (OutputStream os = con.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            try (InputStream is = con.getInputStream()) {
                EmbeddingResponse res = mapper.readValue(is, EmbeddingResponse.class);
                return res.data[0].embedding;
            }

        } catch (Exception e) {
            throw new RuntimeException("Embedding API failed", e);
        }
    }

    static class EmbeddingRequest {
        public String model;
        public String input;
        EmbeddingRequest(String m, String i) {
            model = m; input = i;
        }
    }

    static class EmbeddingResponse {
        public EmbeddingData[] data;
    }

    static class EmbeddingData {
        public float[] embedding;
    }
}
