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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    @FXML
    TextField kTF;
    @FXML
    TextField datasetTF;
    @FXML
    TextField datasetLocationTF;
    @FXML
    TextField datasetFilesTF;
    @FXML
    TextField stoplistlocationTF;
    @FXML
    TextField dataTrainingPercentageTF;
    @FXML
    TextField dataClassificationPercentageTF;
    @FXML
    TextField checkedTagTF;
    @FXML
    TextField checkedLabelsTF;
    @FXML
    TextField metricsTF;
    @FXML
    TextField popularWordsTF;


    @FXML
    Button classify;
    @FXML
    Button stats;


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
        Button button = (Button) evt.getSource();
        final String buttonId = button.getId();
        if (buttonId.equals("classify")) {
            ConfigurationFile file = prepare();
            List<StatisticsData> data = Classifier.classify(file);
            listView.getItems().clear();
            listView.getItems().addAll(data);
        }
        if (buttonId.equals("stats")) {
            StatisticsData temp = listView.getItems().get(listView.getSelectionModel().getSelectedIndex());
//            signalAvgValue.clear();
//
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
        configurationFile.setDatasetDestination(datasetLocationTF.getCharacters().toString());
        configurationFile.setDatasetCounter(Long.parseLong(datasetFilesTF.getCharacters().toString()));
        configurationFile.setDataTrainingPercentage(Integer.parseInt(dataTrainingPercentageTF.getCharacters().toString()));
        configurationFile.setDataClassificationPercentage(Integer.parseInt(dataClassificationPercentageTF.getCharacters().toString()));
        configurationFile.setCheckedTag(checkedTagTF.getCharacters().toString());
        configurationFile.setCheckedTagLabels(checkedLabelsTF.getCharacters().toString());
        configurationFile.setK(Integer.parseInt(kTF.getCharacters().toString()));
        configurationFile.setMetrics(metricsTF.getCharacters().toString());
        configurationFile.setHowManyPopularWordsToCheck(Integer.parseInt(popularWordsTF.getCharacters().toString()));
        return configurationFile;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
