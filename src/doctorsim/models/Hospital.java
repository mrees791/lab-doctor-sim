package doctorsim.models;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Represents a hospital with doctors, available tools, and patients.
 * 
 * @author Michael Rees
 */
public class Hospital {
	private int numberOfDoctors;
    private Doctor[] doctors;
    private Tool[] tools;
    private Queue<Patient> patients;
    private Object toolsLock, patientLock;
    private long startTime, endTime;
    
    public Hospital() {
        toolsLock = new Object();
        patientLock = new Object();
        numberOfDoctors = 4;
    	patients = new LinkedList<Patient>();
    }
    
    public void start() {
        showIntro();
        initializeToolsAndPatients();
        openHospital(numberOfDoctors);
        treatPatients();
    }
    
    /**
     * Explains how to use the program. This is the first thing the user will see.
     */
    private void showIntro() {
        System.out.println("Doctor Sim Lab");
        System.out.println("To begin, you will need to select a patient file.");
        System.out.println("A patient file is a text file with the patient data needed for the simulation.");
        System.out.println("Multiple patient files are included with this program.");
        promptEnterKey();
    }
    
    private void promptEnterKey() {
        System.out.println("Press enter to continue.");
        new Scanner(System.in).nextLine();
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
    	PatientFileParser file = new PatientFileParser();
    	file.start();
    	tools = file.getTools();
    	patients.addAll(file.getPatients());
    }
    
    private void openHospital(int amount) {
        doctors = new Doctor[amount];
        
        for (int i = 0; i < doctors.length; i++) {
            doctors[i] = new Doctor(i, tools, toolsLock);
            doctors[i].start();
        }
        
        System.out.printf("Hospital is Open\n");
    }
}