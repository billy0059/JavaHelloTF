import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.tensorflow.Graph;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Shape;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.*;


public class HelloTF {
	public static void main(String[] args) throws Exception {
		final int EVENT_COUNT = 90;
		final int TIME_STEP = 8+1;
		String modelPath = "C:\\\\Users\\\\Billy_Wang\\\\Desktop\\\\SequencePatternTest\\\\PDR_event_model";
		String indexPath = "C:\\\\Users\\\\Billy_Wang\\\\Desktop\\\\SequencePatternTest\\\\index.txt";
		DataHandler dataHandler = new DataHandler(indexPath);
		try(SavedModelBundle b = SavedModelBundle.load(modelPath, "serve")){
			Session s = b.session();    	
			
			int[] shape = {1,TIME_STEP,EVENT_COUNT};
			String[] events= {"(MODE_FULL)","(IMPORT_MEDIA)","(LIB_MEDIA_CATEGORY)","(ENTER_EFFECT_ROOM)","(ENTER_PIP_ROOM)",
					"(ENTER_PARTICLE_ROOM)","(ENTER_TITLE_ROOM)","(MAGIC_MOVIE)"};
			Tensor inputTensor = dataHandler.getInputVector(shape, events);
			Tensor result = s.runner().feed("x", inputTensor).fetch("pred").run().get(0);
			
			float[][] m = new float[1][EVENT_COUNT];			
			float[][] matrix = result.copyTo(m);
			int predIndex = dataHandler.maxIndex(matrix[0], EVENT_COUNT);
			System.out.println("The previous events are :");
			for (String event:events)
				System.out.println(event);
			String predEvent = dataHandler.getEvent(predIndex);
			System.out.println("Are you tring to do "+predEvent+" ?");
			matrix[0][predIndex] = -10;
			predIndex = dataHandler.maxIndex(matrix[0], EVENT_COUNT);
			predEvent = dataHandler.getEvent(predIndex);
			System.out.println("Or are you tring to do "+predEvent+" ?");
			    	
    	
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
    
}
