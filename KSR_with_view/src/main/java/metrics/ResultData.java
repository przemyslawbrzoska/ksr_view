package metrics;

public class ResultData {
    ExtractedData extractedData;
    String assignedLabel;

    public ResultData(ExtractedData extractedData, String assignedLabel) {
        this.extractedData = extractedData;
        this.assignedLabel = assignedLabel;
    }

    public ResultData(ExtractedData extractedData) {
        this.extractedData = extractedData;
    }

    public ExtractedData getExtractedData() {
        return extractedData;
    }

    public void setExtractedData(ExtractedData extractedData) {
        this.extractedData = extractedData;
    }

    public String getAssignedLabel() {
        return assignedLabel;
    }

    public void setAssignedLabel(String assignedLabel) {
        this.assignedLabel = assignedLabel;
    }
}
