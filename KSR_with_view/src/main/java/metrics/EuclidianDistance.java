package metrics;

import java.util.HashSet;
import java.util.Set;

public class EuclidianDistance implements  OurMetric {

    @Override
    public double getDistance(ExtractedData extractedData1, ExtractedData extractedData2) {
        double distance = 0.0;

        Set<String> setOfKeys = new HashSet<>(extractedData1.features.keySet());
        setOfKeys.addAll(extractedData2.features.keySet());


        for (String key :setOfKeys) {
            double a = extractedData1.features.getOrDefault(key,0.0);
            double b = extractedData2.features.getOrDefault(key,0.0)
                    ;
            distance += Math.sqrt(Math.pow(a-b,2));
        }

        return distance;
    }
}
