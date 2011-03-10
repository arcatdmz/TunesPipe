package jp.digitalmuseum.pipes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TunesPipeConfig implements Serializable {
	private static final long serialVersionUID = -4212746209477763957L;

	public List<TunesPipeInfo> tunesPipeInfo = new ArrayList<TunesPipeInfo>();
	public int frameX;
	public int frameY;
}
