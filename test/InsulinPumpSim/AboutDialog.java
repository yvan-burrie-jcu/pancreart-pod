package InsulinPumpSim;/*
	A basic extension of the java.awt.Dialog class
 */

import java.awt.*;

public class AboutDialog extends Dialog {

	public AboutDialog(Frame parent, boolean modal)
	{
		super(parent, modal);

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
        
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(249,150);
		setVisible(false);
		label1.setText("A Basic Java Application");
		add(label1);
		label1.setBounds(40,35,166,21);
		okButton.setLabel("OK");
		add(okButton);
		okButton.setBounds(95,85,66,27);
		setTitle("AWT Application - About");
		//}}
        
		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		okButton.addActionListener(lSymAction);
		//}}

	}
    
	public AboutDialog(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
                Dimension d = getSize();

		super.addNotify();

		// Only do this once.
		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets insets = getInsets();
		setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets.left, insets.top);
			components[i].setLocation(p);
		}

		// Used for addNotify check.
		fComponentsAdjusted = true;
	}

	public void setVisible(boolean b)
	{
	    if (b)
	    {
    		Rectangle bounds = getParent().getBounds();
    		Rectangle abounds = getBounds();

    		setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
    			 bounds.y + (bounds.height - abounds.height)/2);
	    }

		super.setVisible(b);
	}

	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Button okButton = new java.awt.Button();
	//}}
    
    // Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == okButton)
				okButton_ActionPerformed(event);
		}
	}

	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		okButton_ActionPerformed_Interaction1(event);
	}


	void okButton_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			this.dispose();
		} catch (Exception e) {
		}
	}


	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == AboutDialog.this)
				AboutDialog_WindowClosing(event);
		}
	}

	void AboutDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		AboutDialog_WindowClosing_Interaction1(event);
	}


	void AboutDialog_WindowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			this.dispose();
		} catch (Exception e) {
		}
	}

}
