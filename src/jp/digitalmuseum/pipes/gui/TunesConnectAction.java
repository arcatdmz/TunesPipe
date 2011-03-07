package jp.digitalmuseum.pipes.gui;

import jp.digitalmuseum.pipes.Pipe;
import jp.digitalmuseum.pipes.TunesPipeMain;

public class TunesConnectAction extends AbstractConnectAction {
	private static final long serialVersionUID = 1666957434375646159L;

	public TunesConnectAction() {
		super();
	}

	public TunesConnectAction(String text) {
		super(text);
	}

	protected Pipe getPipeInstance() {
		return TunesPipeMain.getInstance();
	}
}
