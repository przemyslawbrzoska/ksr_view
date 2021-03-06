package sample;

import input.ConfigurationFile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    Classifier classifier = new Classifier();

    @FXML
    TextField kTF;
    @FXML
    TextField datasetTF;
    @FXML
    TextField dataTrainingPercentageTF;
    @FXML
    TextField dataClassificationPercentageTF;
    @FXML
    TextField metricsTF;
    @FXML
    TextField popularWordsTF;


    @FXML
    Button classify;
    @FXML
    Button stats;
    @FXML
    Button train;


    @FXML
    TextField tpTF;
    @FXML
    TextField fpTF;
    @FXML
    TextField tnTF;
    @FXML
    TextField fnTF;
    @FXML
    TextField accuracyTF;
    @FXML
    TextField precisionTF;
    @FXML
    TextField recallTF;
    @FXML
    TextField labelTF;


    @FXML
    CheckBox keyWordsCheck;
    @FXML
    CheckBox keyWordsAllCheck;
    @FXML
    CheckBox stopWordsCheck;
    @FXML
    CheckBox stopWordsFreqCheck;
    @FXML
    CheckBox triGramsCheck;
    @FXML
    CheckBox triGramsFreqsCheck;
    @FXML
    CheckBox tfidfCheck;
    @FXML
    CheckBox avgWord;
    @FXML
    CheckBox word03;
    @FXML
    CheckBox word36;
    @FXML
    CheckBox word6;
    @FXML
    CheckBox firstHalf;
    @FXML
    CheckBox secHalf;



    @FXML
    ListView<StatisticsData> listView = new ListView<>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Classifier");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnCloseRequest(x -> {
            Platform.exit();
        });
        primaryStage.show();

    }

    @FXML
    protected void handleOnAnyButtonClicked(ActionEvent evt) {
        ConfigurationFile file = prepare();
        FeatureDisabler features= new FeatureDisabler();

        Button button = (Button) evt.getSource();
        final String buttonId = button.getId();

        if (buttonId.equals("train")){
            classifier.train(file);
        }

        if (buttonId.equals("classify")) {
            disableFeatures(features);
            List<StatisticsData> data = classifier.classify(file, features);
            listView.getItems().clear();
            listView.getItems().addAll(data);
        }

        if (buttonId.equals("stats")) {
            StatisticsData temp = listView.getItems().get(listView.getSelectionModel().getSelectedIndex());

            labelTF.clear();
            labelTF.appendText(temp.label.toString());
            tpTF.clear();
            tpTF.appendText(temp.values.get("tp").toString());
            tnTF.clear();
            tnTF.appendText(temp.values.get("tn").toString());
            fpTF.clear();
            fpTF.appendText(temp.values.get("fp").toString());
            fnTF.clear();
            fnTF.appendText(temp.values.get("fn").toString());

            accuracyTF.clear();
            accuracyTF.appendText(temp.values.get("accuracy").toString());
            recallTF.clear();
            recallTF.appendText(temp.values.get("recall").toString());
            precisionTF.clear();
            precisionTF.appendText(temp.values.get("precision").toString());

    }
    }

    public ConfigurationFile prepare() {
        ConfigurationFile configurationFile = new ConfigurationFile();
        configurationFile.setDataset(datasetTF.getCharacters().toString());
        if(configurationFile.getDataset().equals("own")){
            configurationFile.setDatasetDestination("C:/Users/Przemysław/Desktop/Nowy folder (2)/KSR_with_view/src/main/java/dataset/biologylove.txt");
            configurationFile.setDatasetCounter(1l);
            configurationFile.setCheckedTag("TOPICS");
            configurationFile.setCheckedTagLabels("biology love");
        }
        else if (configurationFile.getDataset().equals("reutPL")){

            configurationFile.setDatasetDestination("C:\\Users\\Przemysław\\Desktop\\Nowy folder (2)\\ksr_view\\KSR_with_view\\src\\main\\java\\dataset\\");
            configurationFile.setCheckedTag("PLACES");
            configurationFile.setCheckedTagLabels("usa west-germany france canada uk japan");
            configurationFile.setDatasetCounter(22l);
        }
        else{
            configurationFile.setDatasetDestination("C:\\Users\\Przemysław\\Desktop\\Nowy folder (2)\\ksr_view\\KSR_with_view\\src\\main\\java\\dataset\\");
            configurationFile.setCheckedTag("TOPICS");
            configurationFile.setCheckedTagLabels("earn acq");
            configurationFile.setDatasetCounter(22l);
        }
        configurationFile.setDataTrainingPercentage(Integer.parseInt(dataTrainingPercentageTF.getCharacters().toString()));
        configurationFile.setDataClassificationPercentage(Integer.parseInt(dataClassificationPercentageTF.getCharacters().toString()));

        configurationFile.setK(Integer.parseInt(kTF.getCharacters().toString()));
        configurationFile.setMetrics(metricsTF.getCharacters().toString());
        configurationFile.setHowManyPopularWordsToCheck(Integer.parseInt(popularWordsTF.getCharacters().toString()));
        return configurationFile;
    }
    public void disableFeatures(FeatureDisabler feature){

        feature.setAvgWord(!avgWord.isSelected());
        feature.setFirstHalf(!firstHalf.isSelected());
        feature.setSecondHalf(!secHalf.isSelected());
        feature.setWord36(!word36.isSelected());
        feature.setWord03(!word03.isSelected());
        feature.setWord6(!word6.isSelected());

        feature.setKeyWords(!keyWordsCheck.isSelected());
        feature.setKeyWordsFreq(!keyWordsAllCheck.isSelected());
        feature.setStopWords(!stopWordsCheck.isSelected());
        feature.setStopWordsFreq(!stopWordsFreqCheck.isSelected());
//        feature.setNgram(!triGramsCheck.isSelected());
//        feature.setNgramFreq(!triGramsFreqsCheck.isSelected());
        feature.setTfidf(!tfidfCheck.isSelected());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
