import agent.IssueUnderstandingAgent;
import dataModel.IssueFeatures;
import dataModel.IssueRequest;
import llm.LLMClient;

public class Main {
    public static void main(String[] args) {
        IssueRequest request = new IssueRequest();
        request.title = "Payment failures for EU customers";
        request.description = "Multiple users report card payments failing since morning";

        LLMClient llmClient = new LLMClient();
        IssueUnderstandingAgent agent = new IssueUnderstandingAgent(llmClient);

        IssueFeatures features = agent.analyze(request);

        System.out.println("Category: " + features.category);
        System.out.println("Component: " + features.component);
        System.out.println("Urgency: " + features.urgencyHint);
    }
}
