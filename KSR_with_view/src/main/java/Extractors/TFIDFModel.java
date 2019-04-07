package Extractors;

import java.util.HashMap;
import java.util.Map;

public class TFIDFModel {
    private String label;
    private Map<String, Double> model;


    public TFIDFModel(String label) {
        this.label = label;

        model = new HashMap<>();
    }


    public Map<String, Double> getModel(){
        return model;
    }


    public void setModel(Map<String, Double> model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
//    Term frequency – TF(ti, d) – liczba wystąpień słowa ti w dokumencie dj
//     Document frequency – DF(ti) – liczba dokumentów, w których wystąpiło słowo ti
//     Inverse document frequency – IDF(ti) = log(|D|/DF(ti))
//        gdzie |D| to liczba wszystkich rozpatrywanych dokumentów.
//        Ostateczna waga słowa ti w dokumencie dj wynosi:
//        wi = TF(ti, d) • IDF(ti)

