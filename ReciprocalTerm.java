
public class ReciprocalTerm implements Evaluable {
	Evaluable eval;
	
	public ReciprocalTerm (Evaluable evaluable){
		eval = evaluable;
	}
	
	public double value(double x, double y) {
		
		return 1/eval.value(x, y);
	}

}
