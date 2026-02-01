package rag;

import dataModel.HistoricalIssue;
import repository.HistoricalIssueRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimilarIssueFinderService {
    private final HistoricalIssueRepository repository;
    private final EmbeddingServiceInterface embeddingService;

    public SimilarIssueFinderService(HistoricalIssueRepository repository, EmbeddingServiceInterface embeddingClient) {
        this.repository = repository;
        this.embeddingService = embeddingClient;
    }

    //VECTOR SEARCH
    public List<HistoricalIssue> findSimilar(String text, int k) {
        float[] queryEmbedding = embeddingService.embed(text);

        return repository.getHistoricalIssues().stream()
                .sorted(Comparator.comparingDouble(
                        issue -> -SimilaritySearchService.cosineSimilarity(
                                queryEmbedding, issue.embedding)))
                .limit(k)
                .collect(Collectors.toList());
    }
}
