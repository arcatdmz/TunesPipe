package jp.digitalmuseum.pipes.gui;

import jp.digitalmuseum.pipes.Pipe;
import jp.digitalmuseum.pipes.WebPipeMain;

public class WebConnectAction extends AbstractConnectAction {
	private static final long serialVersionUID = -7152931065299269331L;

	public WebConnectAction() {
		super();
	}

	public WebConnectAction(String text) {
		super(text);
	}

	protected Pipe getPipeInstance() {
		return WebPipeMain.getInstance();
	}
}
