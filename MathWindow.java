import javax.swing.JFrame;


@SuppressWarnings("serial")
public class MathWindow extends JFrame {
	
	public MathWindow(){
		setTitle ("Differential Equation Parser");
		setSize (800, 600);	
		setResizable (false);
		add (new MathPanel());
		setTitle ("Differential Equation Parser");
		setVisible (true);
	}
	
}
