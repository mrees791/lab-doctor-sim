package doctorsim.models;

public class Patient {
    private ToolType[] toolsRequired;
    private long timeToTreatMs;
    
    public Patient(long timeToTreatMs, ToolType[] toolsRequired) {
        this.timeToTreatMs = timeToTreatMs;
        this.toolsRequired = toolsRequired;
    }

    public ToolType[] getToolsRequired() {
        return toolsRequired;
    }

    public void setToolsRequired(ToolType[] toolsRequired) {
        this.toolsRequired = toolsRequired;
    }

    public long getTimeToTreatMs() {
        return timeToTreatMs;
    }

    public void setTimeToTreatMs(long timeToTreatMs) {
        this.timeToTreatMs = timeToTreatMs;
    }
}