
public class ExponentialTerm implements Evaluable {

	Evaluable base;
	Evaluable exponent;
	
	public ExponentialTerm (Evaluable b, Evaluable e){
		base = b;
		exponent = e;
	}
	
	public double value(double x, double y) {
		return Math.pow (base.value(x,  y), exponent.value(x, y));
	}

}
