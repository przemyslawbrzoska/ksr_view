package Extractors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import input.Article;

public class StopWordsRemoval {
    private List<String> stopwords;

    public StopWordsRemoval() {
        StringBuilder allFilesRead = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get("C:\\Users\\Przemysław\\Desktop\\RepoKSR\\KSR_1\\KSR_2019_PBTK\\src\\main\\resources\\stopwords.txt"), StandardCharsets.ISO_8859_1)) {
            stream.forEach(allFilesRead::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String words = allFilesRead.toString();
        List<String> wordsList = Arrays.asList(words.split(","));
        stopwords = wordsList;
    }

    public Article removeStopWord(Article article) {
        List<String> toReturn = new ArrayList<>();
        List<String> bodyWords = article.getArticleBody();
        for (String word : bodyWords) {
            if (!stopwords.contains(word))
                toReturn.add(word);
        }
        article.setArticleBody(toReturn);
        article.vector.features.put("Removed words", (double) bodyWords.size() - toReturn.size());
        article.vector.features.put("Removed words/article size", (double) bodyWords.size() - toReturn.size() / toReturn.size());
        return article;
    }

}
