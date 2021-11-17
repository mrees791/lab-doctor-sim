package doctorsim.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileParser {

	private ArrayList<Patient> patients;
	private Tool[] tools;
	
	public Tool[] getTools() {
		return tools;
	}

	public void setTools(Tool[] tools) {
		this.tools = tools;
	}

	public FileParser() {
		patients = new ArrayList<Patient>();
		tools = new Tool[3];
	}
	
	public void start() {
		JFileChooser file = new JFileChooser();
		int chooserResult = file.showOpenDialog(new JFrame());
		if (chooserResult == JFileChooser.APPROVE_OPTION) {
			String fileName = file.getSelectedFile().getAbsolutePath();
			try {
				parseFile(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parseFile(String fileName) throws Exception {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			tools[0] = ParseToolLine(reader);
			tools[1] = ParseToolLine(reader);
			tools[2] = ParseToolLine(reader);
			int patientAmount = Integer.parseInt(reader.readLine());
			
			for (int iPatient = 0; iPatient < patientAmount; iPatient++) {
				patients.add(ParsePatientLine(reader));
			}
		}
	}
	
	private Tool ParseToolLine(BufferedReader reader) throws Exception {
		String[] splits = reader.readLine().split(" ");
		
		int quantity = Integer.parseInt(splits[1]);
				
		switch (splits[0]) {
		case "monitor":
			return new Tool(ToolType.MONITOR, quantity);
		case "scope":
			return new Tool(ToolType.SCOPE, quantity);
		case "needle":
			return new Tool(ToolType.NEEDLE, quantity);
		}
		throw new Exception("Unable to read tool type.");
	}
	
	private Patient ParsePatientLine(BufferedReader reader) throws NumberFormatException, IOException {
		String[] lineSplits = reader.readLine().split(" ");
		long timeToTreat = Long.parseLong(lineSplits[0]);
		ToolType[] toolsRequired = ParsePatientToolsRequired(lineSplits);
		Patient patient = new Patient(timeToTreat, toolsRequired);
		return patient;
	}
	
	private ToolType[] ParsePatientToolsRequired(String[] lineSplits) {
		ToolType[] toolsRequired = new ToolType[3];
		for (int i = 1; i < 4; i++) {
			switch (lineSplits[i]) {
			case "monitor":
				toolsRequired[i-1] = ToolType.MONITOR;
				break;
			case "scope":
				toolsRequired[i-1] = ToolType.SCOPE;
				break;
			case "needle":
				toolsRequired[i-1] = ToolType.NEEDLE;
				break;
			}
		}
		return toolsRequired;
	}
	
	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}
}