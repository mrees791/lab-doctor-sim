package doctorsim.models;

import java.util.ArrayList;

/**
 * Represents a patient with the amount of time needed to treat them and the tools needed.
 * 
 * @author Michael Rees
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
}