package Extractors;

import java.util.ArrayList;

public class TriGramModel {
    private ArrayList<String> model;
    private String label;

    public TriGramModel(String label) {
        this.label = label;
        model = new ArrayList<String>();
    }
    public ArrayList<String> getModel() {
        return model;
    }

    public void setModel(ArrayList<String> model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addToModel(String singleTrigram){
        model.add(singleTrigram);
    }
}
