package jp.digitalmuseum.rt.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jp.digitalmuseum.rt.RemotiTunesMain;

public class DisconnectAction extends AbstractAction {
	private static final long serialVersionUID = 6663061175644284401L;

	public DisconnectAction() {
		super();
	}

	public DisconnectAction(String text) {
		super(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		RemotiTunesMain.getInstance().disconnect();
	}
}
