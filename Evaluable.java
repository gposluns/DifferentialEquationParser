
public interface Evaluable{
	double value (double x, double y);
	default double scaledValue (double x, double y){
		return value (x/10, y/10);
	}
}
