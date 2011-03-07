package jp.digitalmuseum.pipes.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jp.digitalmuseum.pipes.Pipe;

public abstract class AbstractDisconnectAction extends AbstractAction {
	private static final long serialVersionUID = -6434130484346381783L;

	public AbstractDisconnectAction() {
		super();
	}

	public AbstractDisconnectAction(String text) {
		super(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getPipeInstance().disconnect();
	}

	protected abstract Pipe getPipeInstance();
}
