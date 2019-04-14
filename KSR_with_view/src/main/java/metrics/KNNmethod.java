package metrics;


import java.util.*;
import java.util.stream.Collectors;
import org.jooq.lambda.*;

public class KNNmethod {
    int k;
    String[] labels;
    List<ExtractedData> trainingData;

    public KNNmethod(int k, String[] labels, List<ExtractedData> trainingData) {
        this.k = k;
        this.labels = labels;
        this.trainingData = trainingData;
    }
    public void init(List<ExtractedData> testData){
        trainingData.add(testData.remove(0));
        List<String> labelList = new ArrayList<>();
        labelList.add(trainingData.get(0).label);
       for(int i =0; i < testData.size(); i++){
           ExtractedData item = testData.get(i);
            if(!labelList.contains(item.label)){
                trainingData.add(testData.remove(i));
                labelList.add(item.label);
            }
        }

    }
    public List<ResultData> classify(List<ExtractedData> testData, OurMetric metric, int limitSize) {
        //limitSize = 13;
      //  trainingData.clear();
//        for(int i = 0; i < 6; i++){
//
//            trainingData.add(testData.remove(i));
//        }
 //       init(testData);
 //       init(testData);
 //       init(testData);
        List<ResultData> results = new ArrayList<>();
        // sorting and cutting
        if (limitSize != 0) {
            List<ExtractedData> modifiedTrainingData = new ArrayList<>();
            List<ExtractedData> modifiedTestData = new ArrayList<>();
            for (ExtractedData data : testData) {
                ExtractedData tmp = new ExtractedData(data.label);
                tmp.features = data.features.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(limitSize)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                modifiedTestData.add(tmp);
            }

            for (ExtractedData data : trainingData) {
                ExtractedData tmp = new ExtractedData(data.label);
                tmp.features = data.features.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(limitSize)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                modifiedTrainingData.add(tmp);
            }

            testData = modifiedTestData;
            trainingData = modifiedTrainingData;
        }

        testData.parallelStream().forEach(documentToClassify -> {
            ResultData resultData = new ResultData(documentToClassify);
            Map<ExtractedData, Double> nearestNeighbors = new HashMap<>();

            for (ExtractedData dataToCountDistance : trainingData) {
                double distance = metric.getDistance(dataToCountDistance, documentToClassify);
                nearestNeighbors.put(dataToCountDistance, distance);
            }

            List<String> labelsOfNearestNeighbors = nearestNeighbors.entrySet().parallelStream()
                    .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                    .limit(k)
                    .map(e -> e.getKey().label)
                    .collect(Collectors.toList());

            String label = Seq.of(labelsOfNearestNeighbors).mode().get().get(0);
            resultData.setAssignedLabel(label);
            results.add(resultData);
        });
        return results;
    }
}
