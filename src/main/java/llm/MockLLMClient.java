package llm;

public class MockLLMClient implements LLMClientInterface {

    @Override
    public String extractFeatures(String issueText) {
        if (issueText.toLowerCase().contains("down")) {
            return """
            {
              "category": "outage",
              "component": "payments",
              "affectedUsers": "multiple",
              "urgencyHint": "high"
            }
            """;
        }

        return """
        {
          "category": "bug",
          "component": "frontend",
          "affectedUsers": "single",
          "urgencyHint": "medium"
        }
        """;
    }
}