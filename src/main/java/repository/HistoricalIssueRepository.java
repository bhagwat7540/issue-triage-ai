package repository;

import dataModel.HistoricalIssue;

import java.util.List;

public class HistoricalIssueRepository {
    private List<HistoricalIssue> issues;

    public HistoricalIssueRepository(List<HistoricalIssue> issues) {
        this.issues = issues;
    }

    public List<HistoricalIssue> getAll() {
        return issues;
    }
}

