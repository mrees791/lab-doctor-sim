package doctorsim.models;

import java.util.LinkedList;
import java.util.Queue;

public class Hospital {
	private int numberOfDoctors;
    private Doctor[] doctors;
    private Tool[] tools;
    private Queue<Patient> patients;
    private Object toolsLock, patientLock;
    private long startTime, endTime;
    
    public Hospital() {
        numberOfDoctors = 4;
    	patients = new LinkedList<Patient>();
    }
    
    public void start() {
        toolsLock = new Object();
        patientLock = new Object();
        
        initializeToolsAndPatients();
        
        System.out.printf("Hospital is Open\n");
        
        initializeDoctors(numberOfDoctors);
        treatPatients();
    }
    
    private void treatPatients() {
    	startTime = System.currentTimeMillis();
    	
        while (!doctorsFinished()) {
            for (int iDoctor = 0; iDoctor < doctors.length; iDoctor++) {
                Doctor doctor = doctors[iDoctor];
                
                if (!doctor.SeeingPatient()) {
                    synchronized (patientLock) {
                        if (!patients.isEmpty()) {
                            doctor.assignPatient(patients.remove());
                        }
                    }
                }
            }
        }
        
    	endTime = System.currentTimeMillis();
    	long totalTime = endTime - startTime;
    	double seconds = totalTime / 1000.0;
    	System.out.printf("Finished!\nIt took a total of %.2f seconds to treat the patients.", seconds);
    }
    
    private boolean doctorsFinished() {
        if (patients.size() > 0) {
            return false;
        }
        for (int iDoctor = 0; iDoctor < doctors.length; iDoctor++) {
            if (doctors[iDoctor].SeeingPatient()) {
                return false;
            }
        }
        return true;
    }
    
    private void initializeToolsAndPatients() {
    	FileParser file = new FileParser();
    	file.start();
    	tools = file.getTools();
    	patients.addAll(file.getPatients());
    }
    
    private void initializeDoctors(int amount) {
        doctors = new Doctor[amount];
        
        for (int i = 0; i < doctors.length; i++) {
            doctors[i] = new Doctor(i, tools, patients, toolsLock, patientLock);
            doctors[i].start();
        }
    }
}