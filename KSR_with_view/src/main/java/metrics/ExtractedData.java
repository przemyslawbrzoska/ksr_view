package metrics;

import java.util.HashMap;
import java.util.Map;

public class ExtractedData {
    public String label;
    public Map<String,Double> features;

    public ExtractedData(String label){
        this.label = label;
        features = new HashMap<>();
    }
}
