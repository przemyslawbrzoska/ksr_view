package sample;

import java.util.*;

import Extractors.*;
import input.*;
import metrics.*;


import static java.lang.System.*;

public class Classifier {
    public List<Article> training;
    public List<Article> testing;

    List<TriGramModel> triGramModelList = new ArrayList<>();
    List<PopularityWordsModel> popularityWordsModelList = new ArrayList<>();
    List<TFIDFModel> TFIDFModelList = new ArrayList<>();


    public List<StatisticsData> classify(ConfigurationFile configurationFile, FeatureDisabler featureDisabler) {
        InputUtils inputUtils = new InputUtils();
        for(Article art : testing){
            art.vector.features.clear();
        }
        StopWordsRemoval lol = new StopWordsRemoval();
        if(featureDisabler.stopWords)
        for (Article art : testing) {
            lol.removeStopWord(art);
        }
        out.println("removed stopwords");
        if(featureDisabler.avgWord)
        for (Article art : testing) {
            inputUtils.addToVectorAverageWordLength(art);
        }
        if(featureDisabler.word03)
        for (Article art : testing){
            inputUtils.addToVectorHowManyWordsUnderOrEqual3Letters(art);
        }
        if(featureDisabler.word6)
        for(Article art : testing){
            inputUtils.addToVectorHowManyWordsOver6Letters(art);
        }
        if(featureDisabler.word36)
        for(Article art : testing){
            inputUtils.addToVectorHowManyWordsOver3Under6Letters(art);
        }

        for(PopularityWordsModel popularityWordsModel : popularityWordsModelList)
        for(Article art : testing){
            inputUtils.addToVectorHowManyKeyWordsInFirstHalf(art, popularityWordsModel);
        }
        for(PopularityWordsModel popularityWordsModel : popularityWordsModelList)
        for(Article art : testing){
            inputUtils.addToVectorHowManyKeyWordsInSecondHalf(art, popularityWordsModel);
        }

//        for (Article art : testing) {
//            for (TriGramModel model : triGramModelList) {
//                inputUtils.setInVectorHowManyTrigramsMatch(art, model, featureDisabler);
//            }
//        }
        for (Article art : testing) {
            for (PopularityWordsModel model : popularityWordsModelList) {
                inputUtils.setInVectorHowManyPopularityWordsMatch(art, model, featureDisabler);
            }
        }
        out.println("popularity");
        if (featureDisabler.tfidf) {
            for (Article art : testing) {
                for (TFIDFModel model : TFIDFModelList) {
                    inputUtils.setInVectorTFIDFValues(art, model);
                }
            }
        }
        out.println("tfidf");

        List<ExtractedData> extractedDataTest = new ArrayList<>();
        for (Article a : testing) {
            extractedDataTest.add(a.getVector());
        }
        List<ExtractedData> extractedDataTraining = new ArrayList<>();
        int k = configurationFile.getK();

        OurMetric metric = null;
        if (configurationFile.getMetrics().equals("cheb")) {
            metric = new ChebyshewDistance();
        }
        if (configurationFile.getMetrics().equals("manh")) {
            metric = new ManhattanMetric();
        }
        if (configurationFile.getMetrics().equals("eucl")) {
            metric = new EuclidianDistance();
        }
        for (Article a : training) {
            extractedDataTraining.add(a.getVector());

        }
        out.println("cktj");

        //początek klasyfikacji
        long startClassification = currentTimeMillis();
        KNNmethod knn = new KNNmethod(k, new String[]{}, extractedDataTraining);

        List<ResultData> classifiedDocs = knn.classify(extractedDataTest, metric, 0);
        out.println("Sklasyfikowane dokumenty : " + classifiedDocs.size());
        long finishClassification = currentTimeMillis();
        int correctClassifiedDocs = 0;
        int incorrectlyClassifiedDocs = 0;

        for (ResultData doc : classifiedDocs) {
            try {
                if (doc.getAssignedLabel().equals(doc.getExtractedData().label)) {
                    correctClassifiedDocs++;
                } else incorrectlyClassifiedDocs++;
            } catch (NullPointerException e) {
                incorrectlyClassifiedDocs++;

            }
        }
        out.println("Rozmiar zbioru treningowego " + training.size());
        out.println("Rozmiar zbioru testowego " + testing.size());
        out.println("Poprawnie sklasyfikowane dokumenty " + correctClassifiedDocs);
        out.println("NiePoprawnie sklasyfikowane dokumenty " + incorrectlyClassifiedDocs);
        out.println("Poprawne : " + String.format("%.2f", (double) correctClassifiedDocs / classifiedDocs.size() * 100) + "%\n");


        List<StatisticsData> newStatisticsData = new ArrayList<StatisticsData>();
        List<String> labels = Arrays.asList(configurationFile.getCheckedTagLabels().split(" "));
        for (String label : labels) {
            StatisticsData data = new StatisticsData(label);
            double tp = 0;
            double tn = 0;
            double fp = 0;
            double fn = 0;

            for (ResultData doc : classifiedDocs) {
                if (doc != null) {
                    if (doc.getAssignedLabel().equals(label) && doc.getExtractedData().label.equals(label)) {
                        tp++;
                    } else if (!doc.getAssignedLabel().equals(label) && !doc.getExtractedData().label.equals(label)) {
                        tn++;
                    } else if (!doc.getAssignedLabel().equals(label) && doc.getExtractedData().label.equals(label)) {
                        fn++;
                    } else if (doc.getAssignedLabel().equals(label) && !doc.getExtractedData().label.equals(label)) {
                        fp++;
                    }
                }
            }

            double accuracy = (tp + tn) / (tp + tn + fn + fp);
            double precision = (tp) / (tp + fp);
            double recall = (tp) / (tp + fn);
            data.values.put("tp", tp);
            data.values.put("fp", fp);
            data.values.put("tn", tn);
            data.values.put("fn", fn);
            data.values.put("accuracy", accuracy);
            data.values.put("precision", precision);
            data.values.put("recall", recall);

            out.println(label + " tp: " + tp + "\n"
                    + label + " tn: " + tn + "\n"
                    + label + " fp: " + fp + "\n"
                    + label + " fn: " + fn + "\n");
            out.println("Accuracy for label " + label + " " + accuracy * 100 + "%");
            out.println("Precision for label " + label + " " + precision * 100 + "%");
            out.println("Recall for label " + label + " " + recall * 100 + "%");
            newStatisticsData.add(data);
        }

        return newStatisticsData;
    }

    public void train(ConfigurationFile configurationFile) {
        InputUtils inputUtils = new InputUtils();


        triGramModelList = new ArrayList<>();
        popularityWordsModelList = new ArrayList<>();
        TFIDFModelList = new ArrayList<>();


        String articlesRawAll;
        List<ArticleRaw> articles = null;


        if (!configurationFile.getDataset().equals("own")) {
            articlesRawAll = inputUtils.readRawArticlesToString(configurationFile);
            articles = inputUtils.readRawArticlesToList(articlesRawAll, configurationFile);
        } else {
            articlesRawAll = inputUtils.readOwnRawArticlesToString(configurationFile);
            articles = inputUtils.readRawArticlesToList(articlesRawAll, configurationFile);
        }
        out.println("read");
        ArticleRawParser articleRawParser = new ArticleRawParser();
        List<Article> articlesParsed = new ArrayList<Article>();

        for (ArticleRaw art : articles) {
            try {
                articlesParsed.add(articleRawParser.parseArticle(art));
            } catch (Exception e) {
                err.println("pustebody");
            }
        }
        out.println("parsed");
        training = inputUtils.getPartOfAllList(articlesParsed, configurationFile.getDataTrainingPercentage());// configurationFile.getDataTrainingPercentage());
        testing = inputUtils.getPartOfAllList(articlesParsed, configurationFile.getDataClassificationPercentage());

        StopWordsRemoval test = new StopWordsRemoval();
        for (Article art : training) {
            inputUtils.giveLabel(art, configurationFile);
        }

        for (Article art : testing) {
            inputUtils.giveLabel(art, configurationFile);
        }

        out.println("Rozmiar zbioru treningowego: " + training.size());
        out.println("Rozmiar zbioru testowego: " + testing.size());
        out.println("Zbiory zostały podzielone");


        for (Article art : training) {
            test.removeStopWord(art);
        }
        out.println("removed stop words");
        Map<String, List<Article>> buckets = inputUtils.createBuckets(training); //tu mamy artykuly treningowe pogrupowane wedlug badanych etykiet
        out.println("Pogrupowano ");

        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
            popularityWordsModelList.add(inputUtils.createPopularityWordsModel(bucket.getValue(), bucket.getKey(), configurationFile));
        }
        out.println("prioritywords");
//        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
//            triGramModelList.add(inputUtils.createTriGrams(bucket.getValue(), bucket.getKey()));
//        }
//        out.println("trigramy model");
        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
            TFIDFModelList.add(inputUtils.createIDFmodel(bucket.getValue(), bucket.getKey()));
        }

        out.println("Wytrenowano");


    }

}
