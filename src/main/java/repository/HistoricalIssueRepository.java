package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataModel.HistoricalIssue;
import java.io.InputStream;
import java.util.List;

public class HistoricalIssueRepository {
    private final List<HistoricalIssue> issues;

    public HistoricalIssueRepository() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/historical_issues.json");
            this.issues = mapper.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load historical issues", e);
        }
    }

    public List<HistoricalIssue> getHistoricalIssues() {
        return this.issues;
    }
}