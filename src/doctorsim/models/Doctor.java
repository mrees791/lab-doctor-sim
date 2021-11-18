package doctorsim.models;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Represents a doctor who can treat a patient and use tools as needed.
 * Each doctor represents a separate thread.
 * 
 * @author mrees
 */
public class Doctor extends Thread {
    private Tool[] tools;
    private Patient currentPatient;
    private int idNumber;
    private Object toolsLock;
    private Object patientLock;
    private Queue<Patient> patients;
    private boolean seeingPatient;
    
    public int getIdNumber()
    {
        return idNumber;
    }

    public Doctor(int idNumber, Tool[] tools, Queue<Patient> patients, Object toolsLock, Object patientLock) {
        this.idNumber = idNumber;
        this.tools = tools;
        this.patients = patients;
        this.toolsLock = toolsLock;
        this.patientLock = patientLock;
    }
    
    @Override
    public void run() {
    	while(true) {
            try {
    			treatPatient();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public boolean SeeingPatient() {
        return seeingPatient;
    }
    
    private void returnTools() {
        ArrayList<ToolType> requiredTools = currentPatient.getToolsRequired();
        
    	synchronized (toolsLock) {
    		
            for (int iRequiredTool = 0; iRequiredTool < requiredTools.size(); iRequiredTool++) {
                ToolType requiredTool = requiredTools.get(iRequiredTool);
            	
                for (int iTool = 0; iTool < tools.length; iTool++) {
                    Tool tool = tools[iTool];
                    
                    if (tool.getType() == requiredTool) {
                    	tool.placeTool();
                    }
                }
            }
            toolsLock.notify();
    	}
    }
    
    private void releasePatient() {
        
        this.currentPatient = null;
        System.out.printf("Doctor %d released a patient.\n", idNumber);
        seeingPatient = false;
    }
    
    public void assignPatient(Patient patient) {
    	seeingPatient = true;
        this.currentPatient = patient;
    }
    
    public void treatPatient() throws Exception {
        if (seeingPatient) {
            System.out.printf("Doctor %d is waiting on tools.\n", idNumber);

        	synchronized (toolsLock) {
                while (!allToolsAvailable()) {
            		toolsLock.wait();
        		}
    			getTools();
    			toolsLock.notify();
        	}
            System.out.printf("Doctor %d acquired their tools.\n", idNumber);
            System.out.printf("Doctor %d is treating a patient.\n", idNumber);
            
            long sleepStart = System.currentTimeMillis();
            
            try {
                sleep(currentPatient.getTimeToTreatMs());
            	while (true) {
                    long timeSlept = System.currentTimeMillis() - sleepStart;
                    long timeNeededToSleep = currentPatient.getTimeToTreatMs() - timeSlept;
                    if (timeNeededToSleep <= 0) {
                    	break;
                    }
                    sleep(timeNeededToSleep);
            	}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        	returnTools();
            releasePatient();
        }
    }
    
    public void getTools() throws Exception {
    	ArrayList<ToolType> requiredTools = currentPatient.getToolsRequired();
    	
        for (int iRequiredTool = 0; iRequiredTool < requiredTools.size(); iRequiredTool++) {
            ToolType requiredTool = requiredTools.get(iRequiredTool);
            for (int iTool = 0; iTool < tools.length; iTool++) {
                Tool tool = tools[iTool];
                boolean onLastTool = iTool == tools.length - 1;
                
                if (tool.getType() == requiredTool) {
                	tool.takeTool();
                }
            }
        }
    }
    
    private boolean allToolsAvailable() {
    	ArrayList<ToolType> requiredTools = currentPatient.getToolsRequired();
        int toolsFound = 0;
        
        for (int iRequiredTool = 0; iRequiredTool < requiredTools.size(); iRequiredTool++) {
        	ToolType requiredTool = requiredTools.get(iRequiredTool);
            for (int iTool = 0; iTool < tools.length; iTool++) {
                Tool tool = tools[iTool];
                
                if (tool.toolAvailable() && tool.getType() == requiredTool) {
                    toolsFound += 1;
                }
            }
        }
        return toolsFound == requiredTools.size();
    }
}