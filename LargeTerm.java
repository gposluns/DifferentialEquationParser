import java.util.ArrayList;


public class LargeTerm implements Evaluable {
	protected ArrayList<Evaluable> factors;
	
	public LargeTerm (ArrayList<Evaluable> components){
		factors = components;
	}
	
	public LargeTerm (){
		factors = new ArrayList<Evaluable>();
	}
	
	public void add (Evaluable eval){
		factors.add(eval);
	}
	
	public double value (double x, double y){
		double val = 1;
		
		for (Evaluable part:factors){
			val *= part.value(x, y);
		}
		
		return val;
	}
}
 