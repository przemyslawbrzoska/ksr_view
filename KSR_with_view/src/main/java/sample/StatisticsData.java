package sample;

import java.util.HashMap;
import java.util.Map;

public class StatisticsData {
    public String label;
    public Map<String, Double> values;

    public StatisticsData(String label){
        this.label = label;
        values = new HashMap<>();
    }

}
