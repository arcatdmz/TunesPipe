package jp.digitalmuseum.pipes;

import java.io.Serializable;

public class TunesPipeInfo implements Serializable {
	private static final long serialVersionUID = -1794524994209046330L;

	public int localPort = 13689;
	public String remoteHost;
	public int remotePort = 22;
	public String remoteUserName;
	public boolean isAuthPassword = true;
	public String password;
	public String privateKeyFileName;
	public String privateKeyPassword;
}
