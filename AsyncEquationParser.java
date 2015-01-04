
public class AsyncEquationParser extends Thread {
	
	GraphComponent graph;
	double[][] slopePoints;
	double[] points;
	Evaluable eval;
	int initX, initY;
	int width, height;
	
	public AsyncEquationParser (GraphComponent g, Evaluable evaluable){
		graph = g;	
		eval = evaluable;
		width = g.getWidth();
		height = g.getHeight();
	}
	
	public void setEquation (Evaluable evaluable){
		eval = evaluable;
	}
	
	public void setInitialConditions (int x, int y){
		initX = 10*x + width/2;
		initY = 10*y;
	}

	public void run(){
		
		slopePoints = new double[11][11];
		graph.setSlopePoints(slopePoints);
		for (int x = 0; x < 11; x++){
			for (int y = 0; y < 11; y++){
				slopePoints[x][y] = eval.scaledValue(-width/2 + x*width/10, -height/2 + y*height/10);		
				graph.repaint();
			}
		}
		
		points = new double[width];	
		points[initX] = initY;
		graph.setPoints(points);
		for (int x = initX; x < graph.getWidth() - 1; x++){
			points[x + 1] = points[x] + eval.scaledValue(x - width/2,  points[x]);
			graph.repaint();
		}
		for (int x = initX; x > 0; x--){
			points[x-1] = points[x] - eval.scaledValue(x - width/2, points[x]); 
			graph.repaint();
		}
	}
}
