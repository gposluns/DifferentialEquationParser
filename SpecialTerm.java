
public class SpecialTerm implements Evaluable{
	
	enum Type{
		ACOS,
		ASIN,	
		ATAN,
		COS,
		COSH,
		LN,
		LOG,	
		SIN,		
		SINH,
		TAN,
		TANH;		
	}
	
	protected Evaluable arg;
	protected Type type;
	
	public SpecialTerm (Type kind, Evaluable argument){
		type = kind;
		arg = argument;
	}
	
	public double value (double x, double y){
		switch (type){
		case ACOS:
			return Math.acos(arg.value(x, y));
		case ASIN:
			return Math.asin(arg.value(x,y));
		case ATAN:
			return Math.atan(arg.value(x, y));
		case COS:
			return Math.cos(arg.value(x, y));
		case COSH:
			return Math.cosh(arg.value(x, y));
		case LN:
			return Math.log(arg.value(x, y));
		case LOG:
			return Math.log10(arg.value(x, y));
		case SIN:
			return Math.sin(arg.value(x, y));
		case SINH:
			return Math.sinh(arg.value(x, y));
		case TAN:
			return Math.tan(arg.value(x, y));
		case TANH:
			return Math.tanh(arg.value(x, y));
		default: 
			return 0; 
		}
	}
	
	public String toString (){
		String str = "";
		switch (type){
		case ACOS: str = "acos(";
			break;
		case ASIN: str = "asin(";
			break;
		case ATAN: str = "atan(";
			break; 
		case COS: str = "cos(";
			break;
		case COSH: str = "cosh(";
			break;
		case LN: str = "ln(";
			break;
		case LOG: str = "log(";
			break;
		case SIN: str = "sin(";
			break;
		case SINH: str = "sinh(";
			break;
		case TAN: str = "tan(";
			break;
		case TANH: str = "tanh(";
			break;
		default: str = "?(";
			break;
		}
		str = str + arg + ")";
		return str;
	}
}
