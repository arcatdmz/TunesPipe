package jp.digitalmuseum.pipes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WebPipeConfig implements Serializable {
	private static final long serialVersionUID = -1792650985861076336L;

	public List<WebPipeInfo> webPipeInfo = new ArrayList<WebPipeInfo>();
	public int frameX;
	public int frameY;
}
