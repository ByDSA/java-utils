package es.danisales.datastructures;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MutablePairTest {
    public static class NormalValues {
        private Map.Entry<String, Integer> keyValuePair;

        @Before
        public void init() {
            keyValuePair = new MutablePair<>("1", 1);
        }

        @Test
        public void getKey() {
            assertEquals("1", keyValuePair.getKey());
        }

        @Test
        public void getValue() {
            assertEquals((Integer) 1, keyValuePair.getValue());
        }

        @Test
        public void setValue() {
            keyValuePair.setValue(2);
            assertEquals((Integer) 2, keyValuePair.getValue());
        }

        @Test
        public void equals() {
            assertEquals(new MutablePair<>("1", 1), keyValuePair);
        }

        @Test
        public void equalsMapEntry() {
            Map<String, Integer> map = new HashMap<>();
            map.put("1", 1);
            Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
            assertEquals(entry, keyValuePair);
        }

        @Test
        public void compareByKeyM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", -43);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test
        public void compareByKey1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", 8);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test
        public void compareByKey0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("1", 7);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }

        @Test
        public void compareByValueM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", 2);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test
        public void compareByValue1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("0", 2);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test
        public void compareByValue0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("rwer", 1);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }
    }

    public static class KeyNull {
        private Map.Entry<String, Integer> keyValuePair;

        @Before
        public void init() {
            keyValuePair = new MutablePair<>(null, 1);
        }

        @Test
        public void getKey() {
            assertEquals(null, keyValuePair.getKey());
        }

        @Test
        public void equals() {
            assertEquals(new MutablePair<>(null, 1), keyValuePair);
        }

        @Test
        public void equalsMapEntry() {
            Map<String, Integer> map = new HashMap<>();
            map.put(null, 1);
            Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
            assertEquals(entry, keyValuePair);
        }

        @Test(expected = NullPointerException.class)
        public void compareByKeyM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("0", -43);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test(expected = NullPointerException.class)
        public void compareByKey1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", 8);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test(expected = NullPointerException.class)
        public void compareByKey0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>(null, 7);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }

        @Test
        public void compareByValueM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>(null, 2);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test
        public void compareByValue1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>(null, 2);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test
        public void compareByValue0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("1", 1);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }
    }

    public static class ValueNull {
        private Map.Entry<String, Integer> keyValuePair;

        @Before
        public void init() {
            keyValuePair = new MutablePair<>("1", null);
        }

        @Test
        public void getValue() {
            assertNull(keyValuePair.getValue());
        }

        @Test
        public void equals() {
            assertEquals(new MutablePair<>("1", null), keyValuePair);
        }

        @Test
        public void equalsMapEntry() {
            Map<String, Integer> map = new HashMap<>();
            map.put("1", null);
            Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
            assertEquals(entry, keyValuePair);
        }

        @Test
        public void compareByKeyM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", 0);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test
        public void compareByKey1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", 3);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test
        public void compareByKey0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("1", null);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByKey();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }

        @Test(expected = NullPointerException.class)
        public void compareByValueM1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("2", null);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(-1, compare);
        }

        @Test(expected = NullPointerException.class)
        public void compareByValue1() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("0", null);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair2, keyValuePair);
            assertEquals(1, compare);
        }

        @Test(expected = NullPointerException.class)
        public void compareByValue0() {
            Map.Entry<String, Integer> keyValuePair2 = new MutablePair<>("1", 1);
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            int compare = comparator.compare(keyValuePair, keyValuePair2);
            assertEquals(0, compare);
        }
    }

    public static class PairCompatibility {
        @Test
        public void getInmutable() {
            MutablePair<String, Integer> mutablePair = new MutablePair<>("aa", 11);
            Pair<String, Integer> pair = new Pair<>("aa", 11);

            assertEquals(pair, mutablePair.getInmutable());
        }

        @Test
        public void pairToMutable() {
            MutablePair<String, Integer> mutablePair = new MutablePair<>("aa", 11);
            Pair<String, Integer> pair = new Pair<>("aa", 11);
            MutablePair<String, Integer> mutablePair2 = MutablePair.from(pair);

            assertEquals(mutablePair, mutablePair2);
        }
    }
}