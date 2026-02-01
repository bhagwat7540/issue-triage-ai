package agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataModel.IssueFeatures;
import dataModel.IssueRequest;
import llm.LLMClient;
import llm.LLMClientInterface;

public class IssueUnderstandingAgent {

    private final LLMClientInterface llmClient;

    public IssueUnderstandingAgent(LLMClientInterface llmClient) {
        this.llmClient = llmClient;
    }

    public IssueFeatures understand(String issueText) {
        String llmResponse = llmClient.extractFeatures(issueText);

        int jsonStart = llmResponse.indexOf("{");
        int jsonEnd = llmResponse.lastIndexOf("}");

        if (jsonStart != -1 && jsonEnd != -1) {
            llmResponse = llmResponse.substring(jsonStart, jsonEnd + 1);
        }

        IssueFeatures features = new IssueFeatures();
        features.category = extract(llmResponse, "category");
        features.affectedUsers = extract(llmResponse, "affectedUsers");
        features.urgencyHint = extract(llmResponse, "urgencyHint");
        features.component = extract(llmResponse, "component");

        return features;
    }

    private String extract(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex == -1) return "unknown";

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return "unknown";

        int start = colonIndex + 1;

        int commaIndex = json.indexOf(",", start);
        int braceIndex = json.indexOf("}", start);

        int end;
        if (commaIndex != -1) {
            end = commaIndex;
        } else if (braceIndex != -1) {
            end = braceIndex;
        } else {
            return "unknown";
        }

        return json.substring(start, end)
                .replaceAll("[^a-zA-Z]", "")
                .toLowerCase();
    }

}