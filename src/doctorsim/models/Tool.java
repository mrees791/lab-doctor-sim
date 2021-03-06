package doctorsim.models;

/**
 * Represents a tool and the quantity available by the hospital.
 * 
 * @author Michael Rees
 */
public class Tool {
    private ToolType type;
    private int quantity;
    
    public Tool(ToolType type, int quantity) {
    	this.type = type;
        this.quantity = quantity;
    }

    public ToolType getType() {
        return type;
    }
    
    public void takeTool() throws Exception {
    	if (quantity > 0) {
        	quantity -= 1;
    	} else {
    		throw new Exception("A doctor tried to take an unavailable tool.");
    	}
    }
    
    public void placeTool() {
    	quantity += 1;
    }
    
    public boolean toolAvailable() {
    	return quantity > 0;
    }
}