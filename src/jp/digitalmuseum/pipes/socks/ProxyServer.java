package jp.digitalmuseum.pipes.socks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.LocalStreamForwarder;

public class ProxyServer implements Runnable {

	public static final int SOCKS_VERSION = 4;

	public final static int REQUEST_CONNECT = 1;
	public final static int REQUEST_BIND = 2;

	public final static int REPLY_OK = 90;
	public final static int REPLY_REJECTED = 91;
	public final static int REPLY_NO_CONNECT = 92;
	public final static int REPLY_BAD_IDENTD = 93;

	private ExecutorService executorService;
	private Connection connection;
	private ServerSocket serverSocket;
	private int port;
	private int backlog;
	private InetAddress bindAddr;
	private Future<?> future;

	public ProxyServer(Connection connection, int port) {
		this(connection, port, 5);
	}

	public ProxyServer(Connection connection, int port, int backlog) {
		this(connection, port, backlog, null);
	}

	public ProxyServer(Connection connection, int port, int backlog,
			InetAddress bindAddr) {
		executorService = Executors.newCachedThreadPool();
		this.connection = connection;
		this.port = port;
		this.backlog = backlog;
		this.bindAddr = bindAddr;
	}

	public synchronized void start() {
		if (future != null) {
			return;
		}
		future = executorService.submit(this);
	}

	public synchronized void stop() {
		if (future != null) {
			future.cancel(true);
			future = null;
		}
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port, backlog, bindAddr);
			while (true) {
				Socket socket = serverSocket.accept();
				executorService.execute(new Connector(socket));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private class Connector implements Runnable {
		Socket socket;

		public Connector(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {

				// Read request.
				SocksMessage msg = SocksMessage.newInstance(socket
						.getInputStream());

				// Reject request.
				if (msg == null || msg.host == null
						|| msg.command != REQUEST_CONNECT) {
					SocksMessage reply = new SocksMessage(REPLY_REJECTED);
					reply.write(socket.getOutputStream());
					socket.close();
					return;
				}

				// Accept request.
				onConnect(msg, socket);

			} catch (Exception e) {
				// Do nothing.
			}
		}

		private void onConnect(SocksMessage msg, Socket socket) {
			int replyCode = REPLY_OK;
			LocalStreamForwarder lsf = null;

			// Open forwarding port.
			try {
				lsf = connection.createLocalStreamForwarder(msg.host, msg.port);
			} catch (IOException e) {
				replyCode = REPLY_NO_CONNECT;
			}

			// Send reply.
			try {
				SocksMessage reply = new SocksMessage(replyCode, msg.host, msg.port);
				reply.write(socket.getOutputStream());
			} catch (IOException e) {
				// Do nothing.
			} finally {
				try {
					if (replyCode == REPLY_NO_CONNECT) {
						socket.close();
						return;
					}
				} catch (IOException e) {
					// Do nothing.
					return;
				}
			}

			// Start forwarding data.
			try {
				future = executorService.submit(new Forwarder(lsf, socket));
				executorService.execute(new Forwarder(lsf, socket, future));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Forwarder implements Runnable {
		private static final int IDLE_TIMEOUT = 180000;
		private static final int BUFFER_SIZE = 8192;
		private long lastReadTime;
		private LocalStreamForwarder lsf;
		private Socket socket;
		private InputStream in;
		private OutputStream out;
		private Future<?> future;

		public Forwarder(LocalStreamForwarder lsf, Socket socket)
				throws IOException {
			in = lsf.getInputStream();
			out = socket.getOutputStream();
		}

		public Forwarder(LocalStreamForwarder lsf, Socket socket,
				Future<?> future) throws IOException {
			in = socket.getInputStream();
			out = lsf.getOutputStream();
			this.lsf = lsf;
			this.socket = socket;
			this.future = future;
		}

		@Override
		public void run() {
			try {
				lastReadTime = System.currentTimeMillis();
				byte[] buffer = new byte[BUFFER_SIZE];
				int length = 0;
				while (length >= 0) {
					try {
						if (length != 0) {
							out.write(buffer, 0, length);
							out.flush();
						}
						length = in.read(buffer);
						lastReadTime = System.currentTimeMillis();
					} catch (InterruptedIOException iioe) {
						long timeSinceRead = System.currentTimeMillis() - lastReadTime;
						if (timeSinceRead >= IDLE_TIMEOUT) {
							return;
						}
						length = 0;
					}
				}
			} catch (IOException e) {
				// Do nothing.
			}
			if (future != null) {
				try {
					future.get();
					lsf.close();
					socket.close();
				} catch (Exception e) {
					// Do nothing.
				}
			}
		}
	}

	private static class SocksMessage {
		private byte[] data;

		public int version;
		public int command;
		public int port;
		public String host;

		private SocksMessage() {
		}

		public SocksMessage(int command) {
			data = new byte[2];

			data[0] = (byte) 0;
			data[1] = (byte) command;
		}

		public SocksMessage(int command, String host, int port) {
			data = new byte[8];

			data[0] = (byte) 0;
			data[1] = (byte) command;
			data[2] = (byte) (port >> 8);
			data[3] = (byte) port;

			// Convert IP string to byte data.
			int index = 4;
			int stringIndex = 0;
			int nextIndex;
			while ((nextIndex = host.indexOf('.', stringIndex)) >= 0) {
				data[index++] = Integer.valueOf(
						host.substring(stringIndex, nextIndex)).byteValue();
				stringIndex = nextIndex + 1;
			}
			data[index] = Integer.valueOf(host.substring(stringIndex)).byteValue();
		}

		public void write(OutputStream out) throws IOException {
			out.write(data);
		}

		public static SocksMessage newInstance(InputStream in)
				throws IOException {
			DataInputStream din = new DataInputStream(in);
			SocksMessage message = new SocksMessage();

			// Read SOCKS version.
			message.version = din.readUnsignedByte();
			if (message.version != SOCKS_VERSION) {
				return null;
			}

			// Read command and destination port.
			message.command = din.readUnsignedByte();
			message.port = din.readUnsignedShort();

			// Read destination IP address.
			byte[] addr = new byte[4];
			din.readFully(addr);
			StringBuilder builder = new StringBuilder();
			builder.append(addr[0] & 0xFF);
			for (int i = 1; i < 4; ++i) {
				builder.append('.');
				builder.append(addr[i] & 0xFF);
			}
			message.host = builder.toString();

			// Skip user name.
			while (in.read() > 0);

			return message;
		}
	}
}
