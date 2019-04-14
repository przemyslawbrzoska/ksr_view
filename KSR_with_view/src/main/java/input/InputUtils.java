package input;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import Extractors.*;
import sample.FeatureDisabler;

public class InputUtils {
    public long onWhichArticleTrainingListStop = 0;

    public void addToVectorAverageWordLength(Article art) {
        double avg = 0;
        for (String word : art.getArticleBody()) {
            avg += word.length();
        }
        art.vector.features.put("AverageWordLength", avg / art.getArticleBody().size());
    }

    public void addToVectorArticleLength(Article art) {

        art.vector.features.put("ArticleLength", (double) art.getArticleBody().size());
    }


    public void addToVectorHowManyWordsUnderOrEqual3Letters(Article art) {
        int counter = 0;
        for(String word : art.getArticleBody()){
            if (word.length() <= 3){
                counter ++;
            }
        }
        art.vector.features.put("WordsUnderOrEqual3Letters", (double)counter);
    }

    public void addToVectorHowManyWordsOver3Under6Letters(Article art) {
        int counter = 0;
        for(String word : art.getArticleBody()){
            if (word.length() > 3 && word.length() <= 6){
                counter ++;
            }
        }
        art.vector.features.put("WordsOver3UnderEqual6Letters", (double)counter);
    }


    public void addToVectorHowManyWordsOver6Letters(Article art) {
        int counter = 0;
        for(String word : art.getArticleBody()){
            if (word.length() > 6){
                counter ++;
            }
        }
        art.vector.features.put("WordsOver6Letters", (double)counter);
    }

    public void addToVectorHowManyKeyWordsInFirstHalf(Article art, PopularityWordsModel popularityWordsModel) {
        int counter = 0;
        List<String> fullArt = new ArrayList<>();

        for(String word : popularityWordsModel.getExtractedWords()){
            for(int i = 0; i < fullArt.size()/2; i++){
                if(word.equals(fullArt.get(i)))
                    counter++;
            }
        }
        art.vector.features.put("HowManyKeyWordsInFirstHalf", (double)counter);
    }

    public void addToVectorHowManyKeyWordsInSecondHalf(Article art, PopularityWordsModel popularityWordsModel) {
        int counter = 0;
        List<String> fullArt = new ArrayList<>();

        for(String word : popularityWordsModel.getExtractedWords()){
            for(int i = fullArt.size(); i > fullArt.size()/2; i--){
                if(word.equals(fullArt.get(i)))
                    counter++;
            }
        }
        art.vector.features.put("HowManyKeyWordsInFirstHalf", (double)counter);
    }

    public JSONObject readJson(String s) {
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader(s));
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        } catch (Exception e) {
            System.err.println("Could not read json file from destination " + s);
            e.printStackTrace();
        }
        return null;
    }

    public String readRawArticlesToString(ConfigurationFile configurationFile) {
        StringBuilder allFilesRead = new StringBuilder();
        String temp = null;


        for (Integer i = 0; i < configurationFile.getDatasetCounter(); i++) {
            temp = i.toString();
            if (i < 10)
                temp = "0" + i.toString();
            if (i == 0)
                temp = "00";
            String path = configurationFile.getDatasetDestination() + "reut2-0" + temp + ".sgm";
            System.out.println(path);
            try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
                stream.forEach(allFilesRead::append);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allFilesRead.toString();
    }

    public String readOwnRawArticlesToString(ConfigurationFile configurationFile) {
        StringBuilder allFilesRead = new StringBuilder();
        String temp = null;


        for (int i = 0; i < configurationFile.getDatasetCounter(); i++) {
            String path = configurationFile.getDatasetDestination();
            try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
                stream.forEach(allFilesRead::append);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allFilesRead.toString();

    }

    public List<ArticleRaw> readRawArticlesToList(String articlesString, ConfigurationFile configurationFile) {
        List<ArticleRaw> articles = new ArrayList<ArticleRaw>();
        String[] temporal = articlesString.split("</REUTERS>");
        String pattern = "";
        for (String item : temporal) {
            for (String label : configurationFile.getCheckedTagLabels().split(" ")) {
                pattern = "<" + configurationFile.getCheckedTag() + "><D>" + label + "</D></" + configurationFile.getCheckedTag() + ">";
                if (item.contains(pattern)) {
                    articles.add(new ArticleRaw(item));
                }
            }
        }
        Collections.shuffle(articles);
        return articles;

    }

    public List<Article> getPartOfAllList(List<Article> listOfArticles, int percentage) {
        List<Article> extractedArticles = new ArrayList<Article>();
        long d = listOfArticles.size() * percentage / 100 - 1;
        if (onWhichArticleTrainingListStop == 0) {

            for (int i = 0; i < d; i++) {
                extractedArticles.add(listOfArticles.get(i));

            }
        } else {
            for (int i = (int) onWhichArticleTrainingListStop; i < listOfArticles.size() * percentage / 100 - 1 + onWhichArticleTrainingListStop; i++) {
                extractedArticles.add(listOfArticles.get(i));
            }
        }
        onWhichArticleTrainingListStop = d;
        return extractedArticles;
    }

    public List<Article> getSecondPartOfAllList(List<Article> listOfArticles, int percentage) {
        List<Article> extractedArticles = new ArrayList<>();
        for (int i = listOfArticles.size() - 1; i > listOfArticles.size() * percentage / 100; i--) {
            extractedArticles.add(listOfArticles.get(i));
        }
        return extractedArticles;
    }

    public Article giveLabel(Article art, ConfigurationFile configurationFile) {
        if (configurationFile.getCheckedTag().equals("PLACES")) {
            art.vector.label = art.getArticleTagPlaces()[0];
        } else {
            art.vector.label = art.getArticleTagTopics()[0];
        }
        return art;
    }

    public TriGramModel createTriGrams(List<Article> articles, String label) {
        TriGramModel newModel = new TriGramModel(label);
        String[] arrayForSingleTriGram = new String[3];

        for (Article art : articles) {
            for (int i = 0; i < art.getArticleBody().size() - 2; i++) {
                arrayForSingleTriGram[0] = art.getArticleBody().get(i);
                arrayForSingleTriGram[1] = art.getArticleBody().get(i + 1);
                arrayForSingleTriGram[2] = art.getArticleBody().get(i + 2);
                String singleTriGram = arrayForSingleTriGram[0] + " "
                        + arrayForSingleTriGram[1] + " "
                        + arrayForSingleTriGram[2];
                newModel.addToModel(singleTriGram);
            }

        }

        return newModel;
    }

    public PopularityWordsModel createPopularityWordsModel(List<Article> articles, String label, ConfigurationFile configurationFile) {
        PopularityWordsModel newModel = returnAllWordsWithTheirCounters(articles, label);
        //now, how many words we want to check for popularity
        ArrayList<String> listOfNMostPopularWords = new ArrayList<>();

        for (int i = 0; i < configurationFile.getHowManyPopularWordsToCheck(); i++) {
            listOfNMostPopularWords.add(newModel.getModel().get(i).getWord());
        }
        newModel.getModel().clear();
        newModel.setExtractedWords(listOfNMostPopularWords);
        return newModel;
    }


    public PopularityWordsModel returnAllWordsWithTheirCounters(List<Article> articles, String label) {
        PopularityWordsModel newModel = new PopularityWordsModel(label);
        for (Article art : articles) {
            for (String bodyWord : art.getArticleBody()) {
                if (!newModel.contains(bodyWord))
                    newModel.addToModel(new KeyWord(bodyWord, 1));
                else {
                    newModel.increment(bodyWord);
                }
            }
        }
        newModel.getModel().sort(new KeyWordComp());
        return newModel;
    }

    public int DF(List<Article> articles, String word) {
        int counter = 0;
        for (Article art : articles) {
            if (art.getArticleBody().contains(word))
                counter++;
        }
        return counter;
    }

    public int TF(Article art, String word) {
        int counter = 0;
        for (String singleWord : art.getArticleBody()) {
            if (singleWord.equals(word)) {
                counter++;
            }
        }
        return counter;
    }

    public double IDF(List<Article> articles, String word) {
        return Math.log(Math.abs(articles.size()) / DF(articles, word));
    }


    public TFIDFModel createIDFmodel(List<Article> articles, String label) {
        TFIDFModel newModel = new TFIDFModel(label);
        Map<String, Integer> mapaSlow = new HashMap<>();
        Map<String, Integer> mapaSlowDF = new HashMap<>();
        for (Article art : articles) {
            for (String bodyWord : art.getArticleBody()) {
                if (mapaSlow.containsKey(bodyWord)) {
                    int wystapienia = mapaSlow.get(bodyWord);
                    mapaSlow.remove(bodyWord);
                    mapaSlow.put(bodyWord, wystapienia + 1);
                } else {
                    mapaSlow.put(bodyWord, 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : mapaSlow.entrySet()) {
            int counter = 0;
            for (Article art : articles) {
                if (art.getArticleBody().contains(entry.getKey())) {
                    counter++;
                    continue;
                }
            }
            mapaSlowDF.put(entry.getKey(), counter);
        }
        for (Map.Entry<String, Integer> entry : mapaSlow.entrySet()) {
            double log = Math.log(Math.abs(articles.size()) / mapaSlowDF.get(entry.getKey()));
            if (log == 0) {
                log = 0.00000001;
            }
            newModel.getModel().put(entry.getKey(), entry.getValue() / log);
        }
        for (Map.Entry<String, Double> entry : newModel.getModel().entrySet()) {
            if (entry.getValue() > 200) {
                newModel.getModel().entrySet().remove(entry.getKey());
            }
        }

        return newModel; //problem jest taki że teraz muszę się odwołać do każdego badanego artykułu, więc ta metoda w sumie zwraca tylko te liste
    }

    public void setInVectorHowManyTrigramsMatch(Article art, TriGramModel triGramModel, FeatureDisabler featureDisabler) {
        String[] arrayForSingleTriGram = new String[3];
        double matchesCounter = 0;
        for (int i = 0; i < art.getArticleBody().size() - 2; i++) {
            arrayForSingleTriGram[0] = art.getArticleBody().get(i);
            arrayForSingleTriGram[1] = art.getArticleBody().get(i + 1);
            arrayForSingleTriGram[2] = art.getArticleBody().get(i + 2);
            String singleTriGram = arrayForSingleTriGram[0] + " "
                    + arrayForSingleTriGram[1] + " "
                    + arrayForSingleTriGram[2];
            for (String singleTrigramFromModel : triGramModel.getModel()) {
                if (singleTriGram.equals(singleTrigramFromModel)) {
                    matchesCounter++;
                    break;
                }
            }
        }
        if (featureDisabler.isNgram()) {
            art.vector.features.put("Trigrams matched from model " + triGramModel.getLabel(), matchesCounter);
        }
        if (featureDisabler.isNgramFreq()) {
            art.vector.features.put("Trigrams matched from model/how many in article " + triGramModel.getLabel(), matchesCounter / art.getArticleBody().size());
        }
    }

    public void setInVectorHowManyPopularityWordsMatch(Article art, PopularityWordsModel popularityWordsModel, FeatureDisabler featureDisabler) {
        int counterOfMatchedPriorityWords = 0;

        for (String bodyWord : art.getArticleBody()) {
            for (String singleWordFromPopularityModel : popularityWordsModel.getExtractedWords()) {
                if (bodyWord.equals(singleWordFromPopularityModel))
                    counterOfMatchedPriorityWords++;
            }
        }
        if (featureDisabler.isKeyWords()) {
            art.vector.features.put("Matched from priority words " + popularityWordsModel.getLabel(), (double) counterOfMatchedPriorityWords);
        }
        if (featureDisabler.isKeyWordsFreq()) {
            art.vector.features.put("Matched from priority words/all article words " + popularityWordsModel.getLabel(), (double) counterOfMatchedPriorityWords / art.getArticleBody().size());
        }
    }

    public void setInVectorTFIDFValues(Article art, TFIDFModel tfidfModel) {
        double temp = 0;

        for (String bodyWord : art.getArticleBody()) {
            try {
                temp += tfidfModel.getModel().get(bodyWord);
            } catch (NullPointerException e) {

            }

        }

        art.vector.features.put("TFIDF sum for model" + tfidfModel.getLabel(), temp);

    }

    public Map<String, List<Article>> createBuckets(List<Article> articles) {
        Map<String, List<Article>> buckets = new HashMap<>();
        for (Article art : articles) {
            if (buckets.containsKey(art.vector.label)) {
                buckets.get(art.vector.label).add(art);
            } else {
                buckets.put(art.vector.label, new ArrayList<Article>());
                buckets.get(art.vector.label).add(art);
            }
        }
        return buckets;

    }


}
