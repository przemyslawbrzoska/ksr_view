package input;

import org.json.simple.*;

public class ConfigurationFile {
    private int k;
    private String datasetDestination;
    private Long datasetCounter;
    private int dataTrainingPercentage;
    private int dataClassificationPercentage;
    private Boolean createKeyWords;
    private String checkedTag;
    private String checkedTagLabels;
    private int howManyPopularWordsToCheck;
    private String dataset;
    private String metrics;




    public ConfigurationFile(JSONObject configurationFile) {
        k = (int) configurationFile.get("k");
        dataset = (String) configurationFile.get("dataset");
        datasetDestination = (String) configurationFile.get("datasetDestination");
        datasetCounter = (Long) configurationFile.get("datasetCounter");
        dataTrainingPercentage = Integer.parseInt(configurationFile.get("dataTrainingPercentage").toString());
        dataClassificationPercentage = Integer.parseInt(configurationFile.get("dataClassificationPercentage").toString());
        createKeyWords = (Boolean) configurationFile.get("createKeyWords");
        checkedTag = (String) configurationFile.get("checkedTag");
        checkedTagLabels = (String) configurationFile.get("checkedTagLabels");
        howManyPopularWordsToCheck =  Integer.parseInt(configurationFile.get("howManyPopularWords").toString());
    }
   public ConfigurationFile(){
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getMetrics() {
        return metrics;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public String getDatasetDestination() {
        return datasetDestination;
    }

    public void setDatasetDestination(String datasetDestination) {
        this.datasetDestination = datasetDestination;
    }

    public Long getDatasetCounter() {
        return datasetCounter;
    }

    public void setDatasetCounter(Long datasetCounter) {
        this.datasetCounter = datasetCounter;
    }

    public int getDataTrainingPercentage() {
        return dataTrainingPercentage;
    }

    public void setDataTrainingPercentage(int dataTrainingPercentage) {
        this.dataTrainingPercentage = dataTrainingPercentage;
    }

    public int getDataClassificationPercentage() {
        return dataClassificationPercentage;
    }

    public void setDataClassificationPercentage(int dataClassificationPercentage) {
        this.dataClassificationPercentage = dataClassificationPercentage;
    }

    public Boolean getCreateKeyWords() {
        return createKeyWords;
    }

    public void setCreateKeyWords(Boolean createKeyWords) {
        this.createKeyWords = createKeyWords;
    }

    public String getCheckedTag() {
        return checkedTag;
    }

    public void setCheckedTag(String checkedTag) {
        this.checkedTag = checkedTag;
    }

    public String getCheckedTagLabels() {
        return checkedTagLabels;
    }

    public void setCheckedTagLabels(String checkedTagLabels) {
        this.checkedTagLabels = checkedTagLabels;
    }

    public int getHowManyPopularWordsToCheck() {
        return howManyPopularWordsToCheck;
    }

    public void setHowManyPopularWordsToCheck(int howManyPopularWordsToCheck) {
        this.howManyPopularWordsToCheck = howManyPopularWordsToCheck;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "input.ConfigurationFile{" +
                "k=" + k +
                ", datasetDestination='" + datasetDestination + '\'' +
                ", datasetCounter=" + datasetCounter +
                ", dataTrainingPercentage=" + dataTrainingPercentage +
                ", dataClassificationPercentage=" + dataClassificationPercentage +
                ", createKeyWords=" + createKeyWords +
                ", checkedTag='" + checkedTag + '\'' +
                ", checkedTagLabels=" + checkedTagLabels +
                ", howManyPopularWords=" + howManyPopularWordsToCheck +
                '}';
    }
}
