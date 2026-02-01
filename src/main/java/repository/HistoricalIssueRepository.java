package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataModel.HistoricalIssue;
import rag.EmbeddingService;

import java.io.InputStream;
import java.util.List;

public class HistoricalIssueRepository {
    private final List<HistoricalIssue> issues;

    public HistoricalIssueRepository(EmbeddingService embeddingService) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/historical_issues.json");
            this.issues = mapper.readValue(is, new TypeReference<>() {});

            for (HistoricalIssue issue : issues) {
                issue.embedding = embeddingService.embed(issue.text);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load historical issues", e);
        }
    }

    public List<HistoricalIssue> getHistoricalIssues() {
        return this.issues;
    }
}