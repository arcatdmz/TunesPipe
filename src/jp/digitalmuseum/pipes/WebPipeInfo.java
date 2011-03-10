package jp.digitalmuseum.pipes;

import java.io.Serializable;

public class WebPipeInfo implements Serializable {
	private static final long serialVersionUID = 7228946804319145952L;

	public int localPort = 1080;
	public String remoteHost;
	public int remotePort = 22;
	public String remoteUserName;
	public boolean isAuthPassword = true;
	public String password;
	public String privateKeyFileName;
	public String privateKeyPassword;
}
