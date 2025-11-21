package oy.interact.tira.task_08_09;

import oy.interact.tira.student.HashTableDictionary;
import oy.interact.tira.util.Dictionary;
import oy.interact.tira.util.Pair;
import oy.interact.tira.factories.HashTableFactory;

public class HashTableBookImplementation extends BookBase {

    private HashTableDictionary<String, Integer> wordCounts;

    public HashTableBookImplementation() {

    }

    @Override
    protected void createDataStructure() {
        @SuppressWarnings("unchecked")
        Dictionary<String, Integer> dict = HashTableFactory.createHashTable();
        this.wordCounts = (HashTableDictionary<String, Integer>) dict;
    }

    @Override
    protected void insert(final String word) {
        Integer currentCount = wordCounts.get(word);

        if (currentCount == null) {
            wordCounts.add(word, 1);
        } else {
            wordCounts.add(word, currentCount + 1);
        }
    }

    @Override
    protected int uniqueWordCount() {
        if (wordCounts == null) {
            return 0;
        }
        return wordCounts.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Pair<String, Integer>[] getAllWordCounts() {
        if (wordCounts == null) {
            return new Pair[0];
        }
        return wordCounts.toArray();
    }

    @Override
    protected int getWordCount(String word) {
        Integer count = wordCounts.get(word);
        return (count != null) ? count : 0;
    }

    @Override
    protected void resetDataStructure() {
        if (wordCounts != null) {
            wordCounts.clear();
        }
    }

    @Override
    public String getImplementationName() {
        return "Hash Table";
    }

}
