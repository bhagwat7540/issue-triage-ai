package agent;

import dataModel.HistoricalIssue;
import dataModel.IssueFeatures;
import dataModel.PriorityDecision;
import dataModel.Recommendation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecommendationAgent {

    public Recommendation recommend(IssueFeatures features, PriorityDecision decision, List<HistoricalIssue> similarIssues) {
        Recommendation rec = new Recommendation();

        // Pick team based on historical ownership
        String team = mostFrequentTeam(similarIssues);
        rec.assignedTeam = team;

        // Decide action based on priority
        if ("P0".equals(decision.priority) || "P1".equals(decision.priority)) {
            rec.action = "Immediate escalation";
        } else {
            rec.action = "Normal triage queue";
        }

        rec.explanation = buildExplanation(features, decision, team);
        return rec;
    }

    private String mostFrequentTeam(List<HistoricalIssue> issues) {
        return issues.stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.team,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Platform");
    }

    private String buildExplanation(IssueFeatures features, PriorityDecision decision, String team) {
        return String.format(
                "Assigned to %s team based on similar incidents. " +
                        "Priority %s due to %s affecting %s users.",
                team,
                decision.priority,
                features.category,
                features.affectedUsers
        );
    }
}

