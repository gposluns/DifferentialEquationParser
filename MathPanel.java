
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class MathPanel extends JPanel implements ActionListener{
	JTextField equationField, initXField, initYField;
	JButton calculateButton, syntaxButton;
	AsyncEquationParser async;
	GraphComponent graph;
	JLabel dydx, xAxis, topYAxis, bottomYAxis, middleYAxis, initLabel, initXLabel, initYLabel;
	GroupLayout layout;
	JDialog d;
	
	public MathPanel(){
		dydx = new JLabel ("dy/dx=");
		xAxis = new JLabel ("-27.5                                                                                   0                                                                                   27.5");
		topYAxis = new JLabel ("27.2");
		middleYAxis = new JLabel ("0");
		bottomYAxis = new JLabel ("-27.2");
		initLabel = new JLabel ("Initial Conditions");
		initXLabel = new JLabel ("X:");
		initYLabel = new JLabel ("Y:");
		
		equationField = new JTextField (50);
		initXField = new JTextField (3);
		initYField = new JTextField (3);
		
		graph = new GraphComponent();
		
		calculateButton = new JButton ("Calculate!");
		calculateButton.addActionListener (this);
		syntaxButton = new JButton ("Syntax Guide");
		syntaxButton.addActionListener (this);
		
		makeLayout();				
	}
	
	private void makeLayout(){
		layout = new GroupLayout (this);
		layout.setAutoCreateGaps (true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup().
				addGroup(layout.createParallelGroup().
						addComponent(dydx).
						addComponent(topYAxis).
						addComponent(middleYAxis).
						addComponent(bottomYAxis)).
				addGroup(layout.createParallelGroup().
						addComponent(equationField).
						addGroup (layout.createSequentialGroup().
								addGroup(layout.createParallelGroup().
										addComponent (graph, 550, 550, 550).
										addComponent (xAxis)).
								addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).
										addComponent (initLabel).
										addGroup (layout.createSequentialGroup().
												addComponent (initXLabel).
												addComponent (initXField).
												addComponent (initYLabel).
												addComponent (initYField)).
										addComponent (calculateButton).
										addComponent (syntaxButton)))));
		
		layout.setVerticalGroup(layout.createSequentialGroup().
				addGroup (layout.createParallelGroup().
						addComponent (dydx).
						addComponent (equationField, 20, 20, 20)).
				addGroup (layout.createParallelGroup().
						addGroup(layout.createSequentialGroup().
								addComponent(topYAxis).
								addGap(240).
								addComponent(middleYAxis).
								addGap(240).
								addComponent(bottomYAxis)).
						addGroup(layout.createSequentialGroup().
								addComponent (graph).
								addComponent (xAxis)).
						addGroup(layout.createSequentialGroup().
								addComponent (initLabel).
								addGroup (layout.createParallelGroup().
										addComponent (initXLabel).
										addComponent (initXField, 20, 20, 20).
										addComponent (initYLabel).
										addComponent (initYField, 20, 20, 20)).
								addComponent (calculateButton).
								addComponent (syntaxButton))));
		
		setLayout (layout);
	}
	
	public void actionPerformed (ActionEvent ae){
		if (ae.getActionCommand().equals("Calculate!")){
			try{
				async = new AsyncEquationParser (graph, StringParser.parseMath(equationField.getText()));
			}
			catch (IllegalArgumentException e){
				d = new JDialog();
				d.setLayout (new FlowLayout());
				d.setTitle("Syntax Error");
				d.add (new JLabel (e.getMessage()));
				JButton b = new JButton ("Close");
				b.addActionListener (new ActionListener(){
					public void actionPerformed (ActionEvent e){
						d.dispose();
					}});
				d.add (b);
				d.setSize (800, 100);
				d.setVisible (true);
				return;
			}
			try {
				async.setInitialConditions (Integer.parseInt(initXField.getText()), Integer.parseInt(initYField.getText()));
			}
			catch (NumberFormatException e){
				d = new JDialog();
				d.setLayout (new FlowLayout());
				d.setTitle("Syntax Error");
				d.add (new JLabel ("Please enter integer inital conditions"));
				JButton b = new JButton ("Close");
				b.addActionListener (new ActionListener(){
					public void actionPerformed (ActionEvent e){
						d.dispose();
					}});
				d.add (b);
				d.setSize (400, 100);
				d.setVisible (true);
				return;
			}
				async.run();
		}
		else {
			d = new JDialog ();
			d.setLayout(new FlowLayout());
			d.setTitle ("Syntax Guide");
			d.setSize (800, 250);
			d.add (new JLabel ("Enter the right side of your first-order differential equation just as you would write it,"));
			d.add (new JLabel ("in terms of x and y."));
			d.add (new JLabel ("Use + and - to separate terms as usual."));
			d.add (new JLabel ("Place factors beside each other to multiply."));
			d.add (new JLabel ("Prefix a factor with / to divide instead."));
			d.add (new JLabel ("Use ^ to raise the preceding factor to the power of the next."));
			d.add (new JLabel ("Use func(arg) to apply a function to an expression."));
			d.add (new JLabel ("Supported functions are: sin, cos, tan, csc, sec, cot, asin, acos, atan, acsc, asec, acot,"));
			d.add (new JLabel ("sinh, cosh, tanh, csch, sech, coth, asinh, acosh, atanh, acsch, asech, acoth, ln, log."));
			d.add (new JLabel ("Brackets around the argument are mandatory."));
			d.add (new JLabel ("log(arg) is log10 by default.  Use logb(arg) to do log in base b, where b is any expression."));
			d.add (new JLabel ("e and pi are treated as numbers as close as possible to the relevant constant."));
			d.add (new JLabel ("Use brackets to indicate that an expression is 1 factor, so x(y + 1) = xy + x."));
			
			JButton b = new JButton ("Close");
			b.addActionListener (new ActionListener(){
				public void actionPerformed (ActionEvent e){
					d.dispose();
				}});
			d.add (b);
			
			d.setVisible (true);
		}
	}
	
}
