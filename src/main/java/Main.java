import agent.IssueUnderstandingAgent;
import agent.PriorityAgent;
import agent.RecommendationAgent;
import dataModel.*;
import io.github.cdimascio.dotenv.Dotenv;
import llm.LLMClient;
import llm.LLMClientInterface;
import llm.MockLLMClient;
import rag.EmbeddingService;
import rag.SimilarIssueFinderService;
import repository.HistoricalIssueRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("OPENAI_API_KEY");

        if (apiKey == null) {
            throw new RuntimeException("OPENAI_API_KEY not found");
        }

        IssueRequest request = new IssueRequest();
        request.title = "Payment failures for EU customers";
        request.description = "Multiple users report card payments failing since morning";

        LLMClientInterface llmClient = new MockLLMClient();
//        LLMClientInterface llmClient = new LLMClient(apiKey);
        IssueUnderstandingAgent understandingAgent = new IssueUnderstandingAgent(llmClient);

        IssueFeatures features = understandingAgent.understand(request.description);

        System.out.println("Category: " + features.category);
        System.out.println("Component: " + features.component);
        System.out.println("Urgency: " + features.urgencyHint);

        EmbeddingService embeddingClient = new EmbeddingService();
        HistoricalIssueRepository repo = new HistoricalIssueRepository(embeddingClient);
        SimilarIssueFinderService finder = new SimilarIssueFinderService(repo, embeddingClient);

        List<HistoricalIssue> similar =
                finder.findSimilar(request.title + " " + request.description, 2);

        System.out.println("Top similar issues:");
        for (HistoricalIssue hi : similar) {
            System.out.println("- " + hi.text + " [" + hi.priority + "]");
        }

        PriorityAgent priorityAgent = new PriorityAgent();

        PriorityDecision decision = priorityAgent.decide(
                features,
                similar
        );

        System.out.println("Final Priority: " + decision.priority);
        System.out.println("Confidence: " + decision.confidence);
        System.out.println("Explanation: " + decision.explanation);

        // After PriorityAgent decision

        RecommendationAgent recommendationAgent = new RecommendationAgent();

        Recommendation recommendation = recommendationAgent.recommend(
                features,
                decision,
                similar
        );

        System.out.println("Assigned Team: " + recommendation.assignedTeam);
        System.out.println("Action: " + recommendation.action);
        System.out.println("Recommendation: " + recommendation.explanation);
    }
}