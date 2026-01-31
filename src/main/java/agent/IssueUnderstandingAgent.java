package agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataModel.IssueFeatures;
import dataModel.IssueRequest;
import llm.LLMClient;

public class IssueUnderstandingAgent {
    private final LLMClient llmClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public IssueUnderstandingAgent(LLMClient llmClient) {
        this.llmClient = llmClient;
    }

    public IssueFeatures analyze(IssueRequest request) {
        try {
            String response = llmClient.extractIssueFeatures(request.title, request.description);
            return mapper.readValue(response, IssueFeatures.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze issue", e);
        }
    }
}
