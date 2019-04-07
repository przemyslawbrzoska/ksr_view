package input;


import metrics.ExtractedData;

import java.util.List;

public class Article {
    private String[] articleTagTopics;
    private String[] articleTagPlaces;
    private List<String> articleBody;
    public ExtractedData vector = new ExtractedData("");


    public String[] getArticleTagTopics() {
        return articleTagTopics;
    }

    public void setArticleTagTopics(String[] articleTagTopics) {
        this.articleTagTopics = articleTagTopics;
    }

    public String[] getArticleTagPlaces() {
        return articleTagPlaces;
    }

    public void setArticleTagPlaces(String[] articleTagPlaces) {
        this.articleTagPlaces = articleTagPlaces;
    }

    public List<String> getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(List<String> articleBody) {
        this.articleBody = articleBody;
    }

    public ExtractedData getVector() {
        return vector;
    }

    public void setVector(ExtractedData vector) {
        this.vector = vector;
    }
}
