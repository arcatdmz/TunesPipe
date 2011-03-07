package jp.digitalmuseum.rt.gui;

import java.awt.CardLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

public class SwitchCardAction extends AbstractAction {
	private static final long serialVersionUID = 7241340120158508340L;
	private JPanel panel;
	private String name;

	public SwitchCardAction() {
		super();
	}

	public SwitchCardAction(JPanel panel, String name) {
		this.panel = panel;
		this.name = name;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (panel != null) {
			LayoutManager layoutManager = panel.getLayout();
			if (layoutManager instanceof CardLayout) {
				((CardLayout) layoutManager).show(panel, name);
			}
		}
	}
}
