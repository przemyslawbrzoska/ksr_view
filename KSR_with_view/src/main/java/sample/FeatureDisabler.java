package sample;

public class FeatureDisabler {
    boolean stopWords;
    boolean stopWordsFreq;
    boolean ngram;
    boolean ngramFreq;
    boolean tfidf;
    boolean keyWords;
    boolean keyWordsFreq;
    boolean avgWord;
    boolean word03;
    boolean word36;
    boolean word6;
    boolean firstHalf;
    boolean secondHalf;


    public FeatureDisabler(){
        stopWords = true;
        stopWordsFreq = true;
        ngram = true;
        ngramFreq = true;
        tfidf = true;
        keyWords = true;
        keyWordsFreq = true;
        avgWord = true;
        word03 = true;
        word36 = true;
        word6 = true;
        firstHalf = true;
        secondHalf = true;
    }

    public boolean isStopWords() {
        return stopWords;
    }

    public void setStopWords(boolean stopWords) {
        this.stopWords = stopWords;
    }

    public boolean isStopWordsFreq() {
        return stopWordsFreq;
    }

    public void setStopWordsFreq(boolean stopWordsFreq) {
        this.stopWordsFreq = stopWordsFreq;
    }

    public boolean isNgram() {
        return ngram;
    }

    public void setNgram(boolean ngram) {
        this.ngram = ngram;
    }

    public boolean isNgramFreq() {
        return ngramFreq;
    }

    public void setNgramFreq(boolean ngramFreq) {
        this.ngramFreq = ngramFreq;
    }

    public boolean isTfidf() {
        return tfidf;
    }

    public void setTfidf(boolean tfidf) {
        this.tfidf = tfidf;
    }

    public boolean isKeyWords() {
        return keyWords;
    }

    public void setKeyWords(boolean keyWords) {
        this.keyWords = keyWords;
    }

    public boolean isKeyWordsFreq() {
        return keyWordsFreq;
    }

    public void setKeyWordsFreq(boolean keyWordsFreq) {
        this.keyWordsFreq = keyWordsFreq;
    }

    public boolean isAvgWord() {
        return avgWord;
    }

    public void setAvgWord(boolean avgWord) {
        this.avgWord = avgWord;
    }

    public boolean isWord03() {
        return word03;
    }

    public void setWord03(boolean word03) {
        this.word03 = word03;
    }

    public boolean isWord36() {
        return word36;
    }

    public void setWord36(boolean word36) {
        this.word36 = word36;
    }

    public boolean isWord6() {
        return word6;
    }

    public void setWord6(boolean word6) {
        this.word6 = word6;
    }

    public boolean isFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(boolean firstHalf) {
        this.firstHalf = firstHalf;
    }

    public boolean isSecondHalf() {
        return secondHalf;
    }

    public void setSecondHalf(boolean secondHalf) {
        this.secondHalf = secondHalf;
    }
}
