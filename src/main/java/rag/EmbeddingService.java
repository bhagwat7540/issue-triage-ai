package rag;

import java.util.Random;

public class EmbeddingService {
    private static final int DIM = 8; // small for MVP

    public float[] embed(String text) {
        // TODO - LLM call to get embeddings
        Random random = new Random(text.hashCode());
        float[] vector = new float[DIM];
        for (int i = 0; i < DIM; i++) {
            vector[i] = random.nextFloat();
        }
        return vector;
    }
}

