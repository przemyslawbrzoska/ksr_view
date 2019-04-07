package Extractors;

import java.util.Objects;

public class KeyWord {
    String word;
    double occurencies;

    public KeyWord(String word, double occurencies){
        this.word = word;
        this.occurencies = occurencies;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getOccurencies() {
        return occurencies;
    }

    public void setOccurencies(double occurencies) {
        this.occurencies = occurencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyWord keyWord = (KeyWord) o;
        return Objects.equals(word, keyWord.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
