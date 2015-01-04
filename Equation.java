import java.util.ArrayList;

public class Equation implements Evaluable {
	
	protected ArrayList<Evaluable> parts;

	public Equation (ArrayList<Evaluable> terms){
		parts = terms;

	}
	
	public Equation (){
		parts = new ArrayList<Evaluable>();
	}
	
	public void add (Evaluable term){
		parts.add (term);
	}
	
	public double value(double x, double y){
		double val = 0;
		
		for (Evaluable term:parts){
			val += term.value(x, y);
		}
		
		return val;
	}
	
	public String toString() {
		String str = "";
		for (Evaluable eval:parts){
			str = str + "+ " + eval;
		}
		return str;
	}
}
