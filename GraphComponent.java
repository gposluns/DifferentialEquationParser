import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;


@SuppressWarnings("serial")
public class GraphComponent extends JComponent {
	double[][] slopes = new double[11][11];
	double[] points = new double[getWidth()];
	
	public void paint (Graphics g){
		g.setColor (Color.WHITE);
		g.fillRect(0,  0, getWidth(), getHeight());
		g.setColor (Color.BLACK);
		for (int x = 0; x < slopes.length; x++){
			for (int y = 0; y < slopes[x].length; y++){
				if (Double.compare(slopes[x][y], Double.NaN) != 0){
					if (Math.abs(slopes[x][y]) > 50){
						g.drawLine (x*getWidth()/10, (10 - y)*getHeight()/10, x*getWidth()/10, (10 - y)*getHeight()/10 - 5);
					}
					else{
						g.drawLine (x*getWidth()/10, (10 - y)*getHeight()/10, x*getWidth()/10 + 5, (10 - y)*getHeight()/10 - 5*(int)slopes[x][y]);
					}
				}
			}
		}
		g.setColor(Color.RED);
		for (int p = 0; p < points.length - 1; p++){
			g.drawLine (p, -(int)points[p] + getHeight()/2, p + 1, -(int)points[p+1] + getHeight()/2);
		}
	}
	
	public GraphComponent(){}
	
	public void setSlopePoints (double[][] slopePoints){
		slopes = slopePoints;
	}
	
	public void setPoints (double[] p){
		points = p;
	}
}
