package Extractors;

import java.util.ArrayList;

public class PopularityWordsModel {
    private ArrayList<KeyWord> model;
    private ArrayList<String> extractedWords;
    private String label;

    public PopularityWordsModel(String label) {
        this.label = label;
        model = new ArrayList<KeyWord>();
    }

    public ArrayList<KeyWord> getModel() {
        return model;
    }

    public void setModel(ArrayList<KeyWord> model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addToModel(KeyWord singleKeyWord) {
        model.add(singleKeyWord);
    }

    public ArrayList<String> getExtractedWords() {
        return extractedWords;
    }

    public void setExtractedWords(ArrayList<String> extractedWords) {
        this.extractedWords = extractedWords;
    }

    public boolean contains(String word) {
        for (KeyWord checkedWord : model) {
            if (checkedWord.getWord().equals(word))
                return true;
        }
        return false;
    }

    public void increment(String word) {
        for (KeyWord checkedWord : model) {
            if (checkedWord.getWord().equals(word)) {
                checkedWord.setOccurencies(checkedWord.getOccurencies() + 1d);
                break;
            }
        }
    }
}