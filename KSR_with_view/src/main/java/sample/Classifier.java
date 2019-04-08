package sample;

import java.util.*;

import Extractors.*;
import input.*;
import metrics.*;


import static java.lang.System.*;

public class Classifier {
    public static List<StatisticsData> classify(ConfigurationFile configurationFile, FeatureDisabler featureDisabler) {
        out.println(new GregorianCalendar().getTime());
        InputUtils inputUtils = new InputUtils();
        //ConfigurationFile configurationFile = new ConfigurationFile(inputUtils.readJson("src/config.json"));


        String whatTag = configurationFile.getCheckedTag();
        List<String> whatLabels = new ArrayList<>(Arrays.asList(configurationFile.getCheckedTagLabels().split(" ")));

        List<TriGramModel> triGramModelList = new ArrayList<>();
        List<PopularityWordsModel> popularityWordsModelList = new ArrayList<>();
        List<TFIDFModel> TFIDFModelList = new ArrayList<>();


        String articlesRawAll;
        List<ArticleRaw> articles;

        if (!configurationFile.getDataset().equals("own")) {
            articlesRawAll = inputUtils.readRawArticlesToString(configurationFile);
            articles = inputUtils.readRawArticlesToList(articlesRawAll, configurationFile);
        } else {
            articlesRawAll = inputUtils.readOwnRawArticlesToString(configurationFile);
            articles = inputUtils.readRawArticlesToList(articlesRawAll, configurationFile);
        }

        ArticleRawParser articleRawParser = new ArticleRawParser();
        List<Article> articlesParsed = new ArrayList<Article>();

        for (ArticleRaw art : articles) {
            try {
                articlesParsed.add(articleRawParser.parseArticle(art));
            } catch (Exception e) {
                err.println("pustebody");
            }
        }
        StopWordsRemoval test = new StopWordsRemoval();
        for (Article art : articlesParsed) {
            inputUtils.giveLabel(art, configurationFile);
        }
        out.println("labelki");
        for (Article art : articlesParsed) {
            test.removeStopWord(art, featureDisabler);
        }
        out.println("removed stop words");
        List<Article> trainingList = inputUtils.getPartOfAllList(articlesParsed, configurationFile.getDataTrainingPercentage());// configurationFile.getDataTrainingPercentage());
        List<Article> testingList = inputUtils.getPartOfAllList(articlesParsed, configurationFile.getDataClassificationPercentage());
        out.println(trainingList.size());
        out.println(testingList.size());
        Map<String, List<Article>> buckets = inputUtils.createBuckets(trainingList); //tu mamy artykuly treningowe pogrupowane wedlug badanych etykiet
        out.println("kubelki");


        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
            popularityWordsModelList.add(inputUtils.createPopularityWordsModel(bucket.getValue(), bucket.getKey(), configurationFile));
        }
        out.println("prioritywords");
        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
            triGramModelList.add(inputUtils.createTriGrams(bucket.getValue(), bucket.getKey()));
        }
        out.println("trigramy model");
        for (Map.Entry<String, List<Article>> bucket : buckets.entrySet()) {
            TFIDFModelList.add(inputUtils.createIDFmodel(bucket.getValue(), bucket.getKey()));
        }


        for (Article art : testingList) {
            for (TriGramModel model : triGramModelList) {
                inputUtils.setInVectorHowManyTrigramsMatch(art, model, featureDisabler);
            }
        }

        for (Article art : testingList) {
            for (PopularityWordsModel model : popularityWordsModelList) {
                inputUtils.setInVectorHowManyPopularityWordsMatch(art, model,featureDisabler);
            }
        }
        if(featureDisabler.tfidf) {
            for (Article art : testingList) {
                for (TFIDFModel model : TFIDFModelList) {
                    inputUtils.setInVectorTFIDFValues(art, model);
                }
            }
        }



        List<ExtractedData> extractedDataTest = new ArrayList<>();
        for (Article a : testingList) {
            extractedDataTest.add(a.getVector());
        }
        List<ExtractedData> extractedDataTraining = new ArrayList<>();
        int k = configurationFile.getK();

        OurMetric metric = null;
        if (configurationFile.getMetrics().equals("chebyshew")) {
            metric = new ChebyshewDistance();
        }
        if (configurationFile.getMetrics().equals("manhatan")) {
            metric = new ManhattanMetric();
        }
        if (configurationFile.getMetrics().equals("euclides")) {
            metric = new EuclidianDistance();
        }
        for (Article a : trainingList) {
            extractedDataTraining.add(a.getVector());

        }
        long startClassification = currentTimeMillis();
        KNNmethod knn = new KNNmethod(k, new String[]{}, extractedDataTraining);

        List<ResultData> classifiedDocs = knn.classify(extractedDataTest, metric, 0);
        out.println("Sklasyfikowane dokumenty : " + classifiedDocs.size());
        long finishClassification = currentTimeMillis();
        int correctClassifiedDocs = 0;
        int incorrectlyClassifiedDocs = 0;

        for (ResultData doc : classifiedDocs) {
            if (doc.getAssignedLabel().equals(doc.getExtractedData().label)) {
                correctClassifiedDocs++;
            } else incorrectlyClassifiedDocs++;
        }
        out.println("Rozmiar zbioru treningowego " + trainingList.size());
        out.println("Rozmiar zbioru testowego " + testingList.size());
        out.println("Poprawnie sklasyfikowane dokumenty " + correctClassifiedDocs);
        out.println("NiePoprawnie sklasyfikowane dokumenty " + incorrectlyClassifiedDocs);
        out.println("Poprawne : " + String.format("%.2f", (double) correctClassifiedDocs / classifiedDocs.size() * 100) + "%\n");


        List<StatisticsData> newStatisticsData = new ArrayList<StatisticsData>();
        for (String label : whatLabels) {
            StatisticsData data = new StatisticsData(label);
            double tp = 0;
            double tn = 0;
            double fp = 0;
            double fn = 0;

            for (ResultData doc : classifiedDocs) {
                if (doc.getAssignedLabel().equals(label) && doc.getExtractedData().label.equals(label)) {
                    tp++;
                } else if (!doc.getAssignedLabel().equals(label) && !doc.getExtractedData().label.equals(label)) {
                    tn++;
                } else if (doc.getAssignedLabel().equals(label) && !doc.getExtractedData().label.equals(label)) {
                    fn++;
                } else if (!doc.getAssignedLabel().equals(label) && doc.getExtractedData().label.equals(label)) {
                    fp++;
                }
            }
            double accuracy = (tp + tn) / (tp + tn + fn + fp);
            double precision = (tp) / (tp + fp);
            double recall = (tp) / (tp + fn);
            data.values.put("tp", tp);
            data.values.put("fp", fp);
            data.values.put("tn", tn);
            data.values.put("fn", tn);
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


        out.println(new GregorianCalendar().getTime());
        out.println("stop");
        return newStatisticsData;
    }

}
