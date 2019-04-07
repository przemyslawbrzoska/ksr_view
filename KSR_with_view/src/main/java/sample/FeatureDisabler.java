package sample;

public class FeatureDisabler {
    boolean stopWords;
    boolean stopWordsFreq;
    boolean ngram;
    boolean ngramFreq;
    boolean tfidf;
    boolean keyWords;
    boolean keyWordsFreq;


    public FeatureDisabler(){
        stopWords = true;
        stopWordsFreq = true;
        ngram = true;
        ngramFreq = true;
        tfidf = true;
        keyWords = true;
        keyWordsFreq = true;
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
}
