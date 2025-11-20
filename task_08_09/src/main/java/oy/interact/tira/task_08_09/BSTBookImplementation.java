package oy.interact.tira.task_08_09;

import oy.interact.tira.student.BinarySearchTree;
import oy.interact.tira.util.Pair;
import oy.interact.tira.factories.BSTFactory;

public class BSTBookImplementation extends BookBase {

    private BinarySearchTree<String, Integer> wordCounts;

    @Override
    protected void createDataStructure() {
        this.wordCounts = (BinarySearchTree<String, Integer>) BSTFactory.createStringIntegerBST();
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
    protected Pair<String,Integer>[] getAllWordCounts() {
        if (wordCounts == null) {
            return new Pair[0];
        }
        return wordCounts.toArray();
    }

    @Override
    protected void resetDataStructure() {
        if (wordCounts != null) {
            wordCounts.clear();
            wordCounts = null;
        }
    }

    @Override
    public String getImplementationName() {
        return "Binary Search Tree";
    }

    @Override
    protected int getWordCount(String word) {
        Integer count = wordCounts.get(word);
        return (count != null) ? count : 0;
    }

}
