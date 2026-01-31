package llm;

public class LLMClient {

    public String extractIssueFeatures(String title, String description) {
        // TODO - call LLM API -> to fetch json response with categories
        return """
        {
          "category": "outage",
          "affectedUsers": "multiple",
          "component": "payments",
          "urgencyHint": "high"
        }
        """;
    }
}

