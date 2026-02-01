package llm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LLMClient implements LLMClientInterface{

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();

    public LLMClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String extractFeatures(String issueText) {
        String prompt = """
        Extract structured issue features in JSON.

        Fields:
        category: outage | bug | feature
        affectedUsers: single | multiple
        urgencyHint: low | medium | high

        Issue:
        """ + issueText;

        String escapedPrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        String body = """
                {
                  "model": "gpt-4o-mini",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(escapedPrompt);



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("LLM call failed", e);
        }
    }
}
