package jp.digitalmuseum.pipes;

import java.io.Serializable;

public class TunesPipeConfig implements Serializable {
	private static final long serialVersionUID = -7054905792327981060L;

	public int localPort = 13689;
	public String remoteHost;
	public int remotePort = 22;
	public String remoteUserName;
	public boolean isAuthPassword = true;
	public String password;
	public String privateKeyFileName;
	public String privateKeyPassword;
	public int frameX;
	public int frameY;
}
