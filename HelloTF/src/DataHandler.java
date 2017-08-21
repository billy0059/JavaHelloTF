import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.tensorflow.Tensor;

public class DataHandler {
	
	private Map<Integer, String> map;
	
	public DataHandler(String indexPath){
		this.map = new HashMap<Integer, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(indexPath));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    line = br.readLine(); // abandon ignore
		    
		    while (line != null) {
		    	this.map.put(Integer.parseInt((line.split(" ")[1])), line.split(" ")[0]);
				line = br.readLine();
		    }
		    br.close();
		}catch(Exception e) {
		}		
	}
	public String getEvent (Integer key) {
		return this.map.get(key);
	}
	
	public int returnIndex (String value) {
		int index = 0 ;
		for (Integer i : this.map.keySet()) {
			if (this.map.get(i).equals(value))
				return i;
		}
		return index;
	}
	
	public Tensor getInputVector(int shape[], String[] event) {
    	float [][][]input = new float[shape[0]][shape[1]][shape[2]] ;
    	int time = 1;
    	for (String e : event) {
    		input[0][time++][returnIndex(e)] = 1;
    	}
    	return Tensor.create(input);
	}
	
	public int maxIndex (float[] output, int length) {
		int index = 0;
		float max = 0;
		for (int i = 0 ;i<length; i++) {
			if (output[i] > max ) {
				max = output[i];
				index = i;
			}
		}
		return index;
	}
	
}
