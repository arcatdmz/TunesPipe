package jp.digitalmuseum.pipes.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {
	private static final long serialVersionUID = 5754976246732186359L;

	public ExitAction() {
		super();
	}

	public ExitAction(String text) {
		super(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.exit(0);
	}
}
