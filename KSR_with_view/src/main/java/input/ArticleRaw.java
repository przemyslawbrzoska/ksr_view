package input;

//this is class designed to contain splitted large dataset files into single <reuters><\reuters> tags
public class ArticleRaw {
    private String rawText;

    ArticleRaw(String art){
        rawText = art;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }
}
