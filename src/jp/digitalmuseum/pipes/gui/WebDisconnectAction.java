package jp.digitalmuseum.pipes.gui;

import jp.digitalmuseum.pipes.Pipe;
import jp.digitalmuseum.pipes.WebPipeMain;

public class WebDisconnectAction extends AbstractDisconnectAction {
	private static final long serialVersionUID = 6505090494830004016L;

	public WebDisconnectAction() {
		super();
	}

	public WebDisconnectAction(String text) {
		super(text);
	}

	@Override
	protected Pipe getPipeInstance() {
		return WebPipeMain.getInstance();
	}
}
