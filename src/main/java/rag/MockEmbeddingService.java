package rag;

import java.util.Random;

public class MockEmbeddingService implements EmbeddingServiceInterface {

    private static final int DIM = 384;

    @Override
    public float[] embed(String text) {
        Random r = new Random(text.hashCode()); // deterministic
        float[] v = new float[DIM];
        for (int i = 0; i < DIM; i++) {
            v[i] = r.nextFloat();
        }
        return v;
    }
}
