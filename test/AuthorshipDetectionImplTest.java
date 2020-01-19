package test;

import bg.sofia.uni.fmi.mjt.authorship.detection.AuthorshipDetectorImpl;
import bg.sofia.uni.fmi.mjt.authorship.detection.FeatureType;
import bg.sofia.uni.fmi.mjt.authorship.detection.LinguisticSignature;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AuthorshipDetectionImplTest {
    private static String signatures =
            "Agatha Christie, 4.40212537354, 0.103719383127, 0.0534892315963, 10.0836888743, 1.90662947161\n" +
            "Alexandre Dumas, 4.38235547477, 0.049677588873, 0.0212183996175, 15.0054854981, 2.63499369483\n" +
            "Brothers Grim, 3.96868608302, 0.0529378997714, 0.0208217283571, 22.2267197987, 3.4129614094\n" +
            "Charles Dickens, 4.34760725241, 0.0803220950584, 0.0390662700499, 16.2613453121, 2.87721723105\n" +
            "Douglas Adams, 4.33408042189, 0.238435104414, 0.141554321967, 13.2874354561, 1.86574870912\n" +
            "Fyodor Dostoevsky, 4.34066732195, 0.0528571428571, 0.0233414043584, 12.8108273249, 2.16705364781\n" +
            "James Joyce, 4.52346300961, 0.120109917189, 0.0682315429476, 10.9663296918, 1.79667373227\n" +
            "Jane Austen, 4.41553119311, 0.0563451817574, 0.02229943808, 16.8869087498, 2.54817097682\n" +
            "Lewis Carroll, 4.22709528497, 0.111591342227, 0.0537026953444, 16.2728740581, 2.86275565124\n" +
            "Mark Twain, 4.33272222298, 0.117254215021, 0.0633074228159, 14.3548573631, 2.43716268311\n" +
            "Sir Arthur Conan Doyle, 4.16808311494, 0.0822989796874, 0.0394458485444, 14.717564466, 2.2220872148\n" +
            "William Shakespeare, 4.16216957834, 0.105602561171, 0.0575348730848, 9.34707371975, 2.24620146314\n";
    private static double[] weights = {11, 33, 50, 0.4, 4};
    private static AuthorshipDetectorImpl authorshipDetector;

    @BeforeClass
    public static void init() throws UnsupportedEncodingException {
        InputStream signaturesInputStream = new ByteArrayInputStream(signatures.getBytes("UTF-8"));
        authorshipDetector = new AuthorshipDetectorImpl(signaturesInputStream, weights);
    }

    @Test
    public void testCalculatingSimilarity() {
        Map<FeatureType, Double> features1 = new HashMap<>();
        features1.put(FeatureType.AVERAGE_WORD_LENGTH, 4.4);
        features1.put(FeatureType.TYPE_TOKEN_RATIO, 0.1);
        features1.put(FeatureType.HAPAX_LEGOMENA_RATIO, 0.05);
        features1.put(FeatureType.AVERAGE_SENTENCE_LENGTH, 10.0);
        features1.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, 2.0);
        Map<FeatureType, Double> features2 = new HashMap<>();
        features2.put(FeatureType.AVERAGE_WORD_LENGTH, 4.3);
        features2.put(FeatureType.TYPE_TOKEN_RATIO, 0.1);
        features2.put(FeatureType.HAPAX_LEGOMENA_RATIO, 0.04);
        features2.put(FeatureType.AVERAGE_SENTENCE_LENGTH, 16.0);
        features2.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, 4.0);
        LinguisticSignature firstSignature = new LinguisticSignature(features1);
        LinguisticSignature secondSignature = new LinguisticSignature(features2);
        double actual = this.authorshipDetector.calculateSimilarity(firstSignature, secondSignature);
        double expected = 12.0;
        assertEquals(actual, expected, 0.01);
    }
}
