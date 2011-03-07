package jp.digitalmuseum.pipes.gui;

import jp.digitalmuseum.pipes.Pipe;
import jp.digitalmuseum.pipes.TunesPipeMain;

public class TunesDisconnectAction extends AbstractDisconnectAction {
	private static final long serialVersionUID = -5736186247827066444L;

	public TunesDisconnectAction() {
		super();
	}

	public TunesDisconnectAction(String text) {
		super(text);
	}

	@Override
	protected Pipe getPipeInstance() {
		return TunesPipeMain.getInstance();
	}
}
