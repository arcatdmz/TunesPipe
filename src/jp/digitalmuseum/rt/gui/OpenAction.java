package jp.digitalmuseum.rt.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;


public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = -725830848258117532L;
	private JFrame frame;
	private JTextField textField;

	public OpenAction(JFrame frame, JTextField textField) {
		super();
		this.frame = frame;
		this.textField = textField;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JFileChooser chooser = new JFileChooser(".");
		int returnVal = chooser.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (textField != null) {
				textField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
}