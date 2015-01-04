
public class NegativeTerm implements Evaluable {

	private Evaluable eval;
	
	public NegativeTerm (Evaluable evaluable){
		eval = evaluable;
	}
	
	public double value (double x, double y){
		return -eval.value(x,  y);
	}
	
}
