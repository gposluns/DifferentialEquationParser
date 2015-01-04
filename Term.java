
public class Term implements Evaluable{
	
	protected double val;
	protected Equation xPow, yPow;
	
	public Term (double value, Evaluable xPower, Evaluable yPower){
		val = value;
		xPow = new Equation();
		yPow = new Equation();
		xPow.add(xPower);
		yPow.add(yPower);
	}
	
	public Term (double value){
		val = value;
	}
	
	public Term(){
		val = 1;
	}
	
	public void addXPow(Evaluable xPower){
		if (xPow == null){
			xPow = new Equation();
		}
		xPow.add(xPower);
	}
	
	public void addYPow(Evaluable yPower){
		if (yPow == null){
			yPow = new Equation ();
		}
		yPow.add(yPower);
	}
	
	public void addCoeff(double coeff){
		val *= coeff;
	}
	
	public void add (double num){
		val += num;
	}
	
	public double value (double x, double y){
		if (xPow == null && yPow == null){
			return val;
		}
		if (xPow == null){
			return val*Math.pow(y, yPow.value(x, y));
		}
		if (yPow == null){
			return val*Math.pow(x,  xPow.value(x, y));
		}
		return val*Math.pow(x, xPow.value(x, y))*Math.pow(y,yPow.value (x, y));
	}
	
	public String toString(){
		return "" + val + "x^(" + xPow + ")y^(" + yPow + ")";
	}

}
