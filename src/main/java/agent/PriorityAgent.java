package agent;

import dataModel.HistoricalIssue;
import dataModel.IssueFeatures;
import dataModel.PriorityDecision;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriorityAgent {

    public PriorityDecision decide(IssueFeatures features, List<HistoricalIssue> similarIssues) {
        PriorityDecision decision = new PriorityDecision();

        // Count priorities in similar issues
        Map<String, Long> counts = similarIssues.stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.priority,
                        Collectors.counting()
                ));

        // Pick most common historical priority
        String dominantPriority = counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("P3");

        // Apply rule-based overrides
        if (isCritical(features)) {
            decision.priority = "P0";
            decision.confidence = 0.9;
            decision.explanation =
                    "Classified as critical due to outage affecting multiple users.";
            return decision;
        }

        decision.priority = dominantPriority;
        decision.confidence = calculateConfidence(dominantPriority, counts);
        decision.explanation = "Priority inferred from similar historical issues with same characteristics.";

        return decision;
    }

    private boolean isCritical(IssueFeatures features) {
        return "outage".equalsIgnoreCase(features.category)
                && "multiple".equalsIgnoreCase(features.affectedUsers)
                && "high".equalsIgnoreCase(features.urgencyHint);
    }

    private double calculateConfidence(String priority, Map<String, Long> counts) {
        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        return total == 0 ? 0.5 : (double) counts.get(priority) / total;
    }
}

