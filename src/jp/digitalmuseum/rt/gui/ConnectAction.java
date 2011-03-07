package jp.digitalmuseum.rt.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jp.digitalmuseum.rt.RemotiTunesMain;

public class ConnectAction extends AbstractAction {
	private static final long serialVersionUID = 2992275376400169320L;

	public ConnectAction() {
		super();
	}

	public ConnectAction(String text) {
		super(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		RemotiTunesMain.getInstance().connect();
	}
}
