package doctorsim.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
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
		int chooserResult = file.showOpenDialog(createOpenDialog());
		if (chooserResult == JFileChooser.APPROVE_OPTION) {
			String fileName = file.getSelectedFile().getAbsolutePath();
			try {
				parseFile(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private JDialog createOpenDialog() {
		JDialog openDialog = new JDialog();
		openDialog.setModal(true);
		openDialog.setAlwaysOnTop(true);
		openDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		return openDialog;
	}
	
	private void parseFile(String fileName) throws Exception {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			skipCommentLine(reader);
			parseAllToolLines(reader);
			skipCommentLine(reader);
			parseAllPatientLines(reader);
		}
	}
	
	private void skipCommentLine(BufferedReader reader) throws Exception {
		reader.readLine();
	}

	private void parseAllPatientLines(BufferedReader reader) throws Exception {
		String patientLine = "";
		while ((patientLine = reader.readLine()) != null) {
			patients.add(parsePatientLine(patientLine));
		}
	}
	
	private void parseAllToolLines(BufferedReader reader) throws Exception {
		tools[0] = parseToolLine(reader);
		tools[1] = parseToolLine(reader);
		tools[2] = parseToolLine(reader);
	}
	
	private Tool parseToolLine(BufferedReader reader) throws Exception {
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
	
	private Patient parsePatientLine(String patientLine) throws NumberFormatException, IOException {
		String[] lineSplits = patientLine.split(" ");
		long timeToTreat = Long.parseLong(lineSplits[0]);
		ArrayList<ToolType> toolsRequired = parsePatientToolsRequired(lineSplits);
		return new Patient(timeToTreat, toolsRequired);
	}
	
	private ArrayList<ToolType> parsePatientToolsRequired(String[] lineSplits) {
		ArrayList<ToolType> toolsRequired = new ArrayList<ToolType>();
		for (int i = 0; i < lineSplits.length; i++) {
			switch (lineSplits[i]) {
			case "monitor":
				toolsRequired.add(ToolType.MONITOR);
				break;
			case "scope":
				toolsRequired.add(ToolType.SCOPE);
				break;
			case "needle":
				toolsRequired.add(ToolType.NEEDLE);
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