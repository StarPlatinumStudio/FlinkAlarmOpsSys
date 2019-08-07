package Entity;

public class WordWithCount {

    private String word;
    private long count;

    public WordWithCount() {}

    public WordWithCount(String word, long count) {
        this.word = word;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word + " : " + count;
    }
}