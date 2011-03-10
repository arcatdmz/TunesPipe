package jp.digitalmuseum.pipes;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jp.digitalmuseum.pipes.gui.TunesConnectAction;
import jp.digitalmuseum.pipes.gui.TunesDisconnectAction;
import jp.digitalmuseum.pipes.gui.ExitAction;
import jp.digitalmuseum.pipes.gui.TunesPipeFrame;

import com.trilead.ssh2.Connection;

public class TunesPipeMain implements Pipe {
	public static final String CONFIG_FILENAME = "config";
	public static final String ICON_FILENAME = "resources/tunespipe.png";
	private static TunesPipeMain instance;
	private ExecutorService executor;

	private JmDNS rendezvous;
	private Connection connection;
	private ServiceInfo serviceInfo;

	private TunesPipeFrame frame;
	private boolean isOSX;

	private JPopupMenu menu;
	private JMenuItem connectMenuItem;
	private JMenuItem disconnectMenuItem;
	private TrayIcon trayIcon;
	private boolean isFirstConnection = true;

	public static void main(String[] args) {
		TunesPipeMain.getInstance();
	}

	public static TunesPipeMain getInstance() {
		if (instance == null) {
			instance = new TunesPipeMain();
		}
		return instance;
	}

	private TunesPipeMain() {
		Runtime.getRuntime().addShutdownHook(new TunesShutdownHook());
		executor = Executors.newSingleThreadExecutor();
		isOSX = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initGUI();
				loadConfiguration();
				updateGUI(false);
				frame.setVisible(true);
			}
		});
	}

	private void loadConfiguration() {
		TunesPipeConfig config = null;
		File configFile = new File(CONFIG_FILENAME);
		if (configFile.exists() && configFile.isFile()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(configFile));
				config = (TunesPipeConfig) ois.readObject();
			} catch (Exception e) {
				// Do nothing.
			}
		}
		if (config == null) {
			config = new TunesPipeConfig();
			config.tunesPipeInfo.add(new TunesPipeInfo());
			Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getMaximumWindowBounds();
			config.frameX = (int) (rect.getWidth() - frame.getWidth())/2;
			config.frameY = (int) (rect.getHeight() - frame.getHeight())/2;
		}
		frame.applyConfiguration(config);
	}

	private void saveConfiguration(TunesPipeConfig configuration) {
		File configFile = new File(CONFIG_FILENAME);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(configFile));
			oos.writeObject(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		executor.execute(new Runnable() {

			public void run() {
				if (connection == null &&
						rendezvous == null) {
					frame.setEnabled(false);
					TunesPipeConfig config = frame.getCurrentConfiguration(true);
					TunesPipeInfo info = config.tunesPipeInfo.get(0);
					try {
						frame.setStatusText("リモートサーバに接続しています...");
						setupSSH(info);
						frame.setStatusText("iTunesサービスを公開しています...");
						setupRendezvous(info);
						frame.setStatusText("接続が確立されました.");
						updateGUI(true);
					} catch (Exception e) {
						String message = e.getMessage();
						JOptionPane.showMessageDialog(frame, message);
						frame.setStatusText(message);
						frame.setConnected(false);
						disconnect();
						updateGUI(false);
					}
				}
			}
		});
	}

	public void disconnect() {
		executor.execute(new Runnable() {

			public void run() {
				if (connection != null ||
						rendezvous != null) {
					frame.setEnabled(false);
					frame.setStatusText("接続を切っています...");
					if (connection != null) {
						connection.close();
						connection = null;
					}
					if (rendezvous != null) {
						rendezvous.unregisterService(serviceInfo);
						rendezvous = null;
					}
					frame.setStatusText("接続を切りました.");
					updateGUI(false);
				}
			}
		});
	}

	private void setupSSH(TunesPipeInfo config) throws Exception {
		connection = new Connection(config.remoteHost, config.remotePort);
		try {
			connection.connect();
		} catch (Exception e) {
			throw new Exception("リモートサーバへの接続に失敗しました.", e);
		}
		boolean isAuthenticated = false;
		try {
			isAuthenticated = config.isAuthPassword ?
					connection.authenticateWithPassword(
							config.remoteUserName,
							config.password) :
					connection.authenticateWithPublicKey(
							config.remoteUserName,
							new File(config.privateKeyFileName),
							config.privateKeyPassword);
		} catch (IOException e) {
			throw new Exception("リモートサーバのユーザ認証に失敗しました.", e);
		}
		if (isAuthenticated) {
			connection.createLocalPortForwarder(config.localPort, "127.0.0.1", 3689);
		} else {
			throw new Exception("リモートサーバでユーザ認証が拒否されました.");
		}
	}

	private void setupRendezvous(TunesPipeInfo config)
			throws IOException {
		serviceInfo = ServiceInfo
				.create("_daap._tcp.local.", "TunesPipe", config.localPort,
						"iTunes remote proxy service.");
		rendezvous = JmDNS.create();
		rendezvous.registerService(serviceInfo);
	}

	private void initGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Image iconImage;
		try {
			iconImage = ImageIO.read(getClass()
					.getClassLoader().getResource(ICON_FILENAME));
		} catch (Exception e) {
			try {
				iconImage = ImageIO.read(new File(ICON_FILENAME));
			} catch (IOException e1) {
				iconImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			}
		}

		frame = new TunesPipeFrame();
		frame.setIconImage(iconImage);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (isOSX) {
			return;
		}

		menu = new JPopupMenu();
		connectMenuItem = menu.add(new TunesConnectAction("接続する"));
		disconnectMenuItem = menu.add(new TunesDisconnectAction("切断する"));
		menu.addSeparator();
		menu.add(new ExitAction("終了する"));

		trayIcon = new TrayIcon(iconImage,
				"RemotiTunes / クリックでウィンドウ表示・非表示切り換え");
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.BUTTON1_MASK) ==
						InputEvent.BUTTON1_MASK) {
					frame.setVisible(
							!frame.isVisible());
					if (frame.isVisible()) {
						frame.requestFocus();
					}
				} else {
					/*
					 * http://weblogs.java.net/blog/2006/05/04/using-jpopupmenu-trayicon
					 */
					if (e.isPopupTrigger()) {
						menu.setLocation(e.getX(), e.getY());
						menu.setInvoker(menu);
						menu.setVisible(true);
					}
				}
			}
		});
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void updateGUI(boolean isConnected) {
		frame.setConnected(isConnected);
		frame.setEnabled(true);

		if (isOSX) {
			return;
		}

		frame.setVisible(!isConnected);

		connectMenuItem.setEnabled(!isConnected);
		disconnectMenuItem.setEnabled(isConnected);
		if (isConnected && isFirstConnection) {
			isFirstConnection = false;
			trayIcon.displayMessage("接続に成功", "トレイアイコンをクリックすると、ウィンドウの表示・非表示を切り替えられます。", MessageType.INFO);
		}
	}

	private class TunesShutdownHook extends Thread {

		@Override
		public void run() {
			TunesPipeConfig config = frame.getCurrentConfiguration(false);
			config.frameX = frame.getX();
			config.frameY = frame.getY();
			saveConfiguration(config);
			if (connection != null) {
				connection.close();
			}
			if (rendezvous != null) {
				rendezvous.unregisterService(serviceInfo);
			}
			executor.shutdownNow();
		}

	}
}
