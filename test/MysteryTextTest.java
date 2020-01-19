package test;

import bg.sofia.uni.fmi.mjt.authorship.detection.MysteryText;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MysteryTextTest {
    private static MysteryText mysteryText;

    @BeforeClass
    public static void init() throws UnsupportedEncodingException {
        Map<String, Integer> dictionary = new HashMap<>();
        var text =
            "This eBook is for the use of anyone anywhere at no cost and with\n"
            + "almost no restrictions whatsoever.  You may copy it, give it away or\n"
            + "re-use it under the terms of the Project Gutenberg License included\n"
            + "with this eBook or online at www.gutenberg.org";
        InputStream mysteryTextStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
        mysteryText = new MysteryText(mysteryTextStream);
    }

    @Test
    public void testCalculatingverageWordLength() {
        Double actual = mysteryText.getAverageWordLength();
        Double expected = 4.34;
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testCalculatingTypeTokenRatio() {
        Double actual = mysteryText.getTypeTokenRatio();
        Double expected = 0.73;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testCalculatingHapaxLegomenaRatio() {
        Double actual = mysteryText.getHapaxLegomenaRatio();
        Double expected = 0.52;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testCalculatingAverageWordsPerSentence() {
        Double actual = mysteryText.getAverageSentenceLength();
        Double expected = 11.5;
        assertEquals(actual, expected);
    }

    @Test
    public void testCalculatingAverageSentenceComplexity() {
        Double actual = mysteryText.getAverageSentenceComplexity();
        Double expected = 1.25;
        assertEquals(actual, expected, 0.01);
    }
}
