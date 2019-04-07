package metrics;
import java.util.*;

public class ChebyshewDistance implements OurMetric {

@Override
    public double getDistance(ExtractedData extractedData1, ExtractedData extractedData2){

        Set<String> setOfKeys = new HashSet<>(extractedData1.features.keySet());
        setOfKeys.addAll(extractedData2.features.keySet());

        List<Double> distances = new ArrayList<>();

        for (String key : setOfKeys) {
            double aValue = extractedData1.features.getOrDefault(key, 0.0);
            double bValue = extractedData2.features.getOrDefault(key, 0.0);
            distances.add(Math.abs(aValue - bValue));
        }

        return Collections.max(distances);
    }


}



