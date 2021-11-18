package doctorsim.models;

import java.util.ArrayList;

/**
 * Represents a patient and stores the amount of time they will take to treat and the tools needed to treat them.
 * 
 * @author mrees
 */
public class Patient {
    private ArrayList<ToolType> toolsRequired;
    private long timeToTreatMs;
    
    public Patient(long timeToTreatMs, ArrayList<ToolType> toolsRequired) {
        this.timeToTreatMs = timeToTreatMs;
        this.toolsRequired = toolsRequired;
    }

    public ArrayList<ToolType> getToolsRequired() {
        return toolsRequired;
    }

    public long getTimeToTreatMs() {
        return timeToTreatMs;
    }

    public void setTimeToTreatMs(long timeToTreatMs) {
        this.timeToTreatMs = timeToTreatMs;
    }
}