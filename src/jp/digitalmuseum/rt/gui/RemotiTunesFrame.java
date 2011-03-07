package jp.digitalmuseum.rt.gui;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JRadioButton;
import java.awt.CardLayout;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Insets;

import jp.digitalmuseum.rt.RemotiTunesConfiguration;

public class RemotiTunesFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jLocalSettingPanel = null;
	private JPanel jRemoteSettingPanel = null;
	private JLabel jLocalPortLabel = null;
	private JTextField jLocalPortTextField = null;
	private JLabel jHostAndPortLabel = null;
	private JTextField jHostTextField = null;
	private JLabel jUserNameLabel = null;
	private JTextField jUserNameTextField = null;
	private JLabel jAuthenticationTypeLabel = null;
	private JPanel jAuthenticationTypePanel = null;
	private JRadioButton jAuthPassRadioButton = null;
	private JRadioButton jAuthPublicKeyRadioButton = null;
	private JPanel jAuthenticationOptionPanel = null;
	private JPanel jAuthPasswordPanel = null;
	private JLabel jAuthPasswordLabel = null;
	private JPasswordField jAuthPasswordField = null;
	private JPanel jAuthPublicKeyPanel = null;
	private JLabel jAuthPublicKeyLabel = null;
	private JTextField jAuthPublicKeyTextField = null;
	private JButton jAuthPublicKeyButton = null;
	private JPanel jOperationPanel = null;
	private JButton jConnectButton = null;
	private JButton jDisconnectButton = null;
	private JPanel jHostAndPortPanel = null;
	private JLabel jAndLabel = null;
	private JTextField jPortTextField = null;
	private JLabel jStatusLabel = null;

	private JPanel jAuthPublicKeyPanel1 = null;
	private JPanel jAuthPublicKeyPanel2 = null;
	private JLabel jAuthPublicKeyPasswordLabel = null;
	private JPasswordField jAuthPublicKeyPasswordField = null;

	/**
	 * This is the default constructor
	 */
	public RemotiTunesFrame() {
		super();
		initialize();
	}

	public void applyConfiguration(RemotiTunesConfiguration config) {
		getJLocalPortTextField().setText(String.valueOf(config.localPort));
		getJHostTextField().setText(config.remoteHost);
		getJPortTextField().setText(String.valueOf(config.remotePort));
		getJUserNameTextField().setText(config.remoteUserName);
		CardLayout cardLayout = (CardLayout) getJAuthenticationOptionPanel().getLayout();
		if (config.isAuthPassword) {
			getJAuthPassRadioButton().setSelected(true);
			cardLayout.show(
					getJAuthenticationOptionPanel(),
					getJAuthPasswordPanel().getName());
		} else {
			getJAuthPublicKeyRadioButton().setSelected(true);
			cardLayout.show(
					getJAuthenticationOptionPanel(),
					getJAuthPublicKeyPanel().getName());
		}
		getJAuthPasswordField().setText(config.password);
		getJAuthPublicKeyTextField().setText(config.privateKeyFileName);
		getJAuthPublicKeyPasswordField().setText(config.privateKeyPassword);
		setLocation(config.frameX, config.frameY);
	}

	public RemotiTunesConfiguration getCurrentConfiguration(boolean showMessage) {
		RemotiTunesConfiguration config = new RemotiTunesConfiguration();
		JComponent focusComponent = null;

		// Check local port.
		try {
			config.localPort = Integer.valueOf(
					getJLocalPortTextField().getText());
		} catch (NumberFormatException nfe) {
			if (showMessage) {
				JOptionPane.showMessageDialog(this,
						"ローカルホストのポートは半角数字で入力してください.");
				focusComponent = getJLocalPortTextField();
			}
		}

		// Check remote port.
		try {
			config.remotePort = Integer.valueOf(
					getJPortTextField().getText());
		} catch (NumberFormatException nfe) {
			if (showMessage) {
				JOptionPane.showMessageDialog(this,
				"リモートホストのポートは半角数字で入力してください.");
				focusComponent = getJPortTextField();
			}
		}

		config.remoteHost = getJHostTextField().getText();
		config.remoteUserName = getJUserNameTextField().getText();
		config.isAuthPassword = getJAuthPassRadioButton().isSelected();
		config.password = String.valueOf(getJAuthPasswordField().getPassword());
		config.privateKeyFileName = getJAuthPublicKeyTextField().getText();
		config.privateKeyPassword = String.valueOf(getJAuthPublicKeyPasswordField().getPassword());

		// Request focus if needed.
		if (focusComponent != null) {
			focusComponent.requestFocusInWindow();
		}
		return config;
	}

	public void setStatusText(String text) {
		getJOperationPanel();
		jStatusLabel.setText(text);
	}

	public void setConnected(boolean isConnected) {

		// Local configuration
		getJLocalPortTextField().setEnabled(!isConnected);

		// Remote configuration
		getJHostTextField().setEnabled(!isConnected);
		getJPortTextField().setEnabled(!isConnected);
		getJUserNameTextField().setEnabled(!isConnected);
		getJAuthPassRadioButton().setEnabled(!isConnected);
		getJAuthPublicKeyRadioButton().setEnabled(!isConnected);
		getJAuthPasswordField().setEnabled(!isConnected);
		getJAuthPublicKeyTextField().setEnabled(!isConnected);
		getJAuthPublicKeyButton().setEnabled(!isConnected);
		getJAuthPublicKeyPasswordField().setEnabled(!isConnected);

		// Connect/Disconnect button
		getJConnectButton().setEnabled(!isConnected);
		getJDisconnectButton().setEnabled(isConnected);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(560, 380);
		this.setMinimumSize(new Dimension(560, 380));
		this.setContentPane(getJContentPane());
		this.setTitle("RemotiTunes");
		ButtonGroup group = new ButtonGroup();
		group.add(getJAuthPassRadioButton());
		group.add(getJAuthPublicKeyRadioButton());
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(getJLocalSettingPanel(), null);
			jContentPane.add(getJRemoteSettingPanel(), null);
			jContentPane.add(getJOperationPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jLocalSettingPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJLocalSettingPanel() {
		if (jLocalSettingPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			jLocalPortLabel = new JLabel();
			jLocalPortLabel.setText("ポート:");
			jLocalPortLabel.setPreferredSize(new Dimension(100, 13));
			jLocalSettingPanel = new JPanel();
			jLocalSettingPanel.setLayout(new GridBagLayout());
			jLocalSettingPanel.setBorder(BorderFactory.createTitledBorder(null, "ローカルホストの設定 (DAAP)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("MS UI Gothic", Font.PLAIN, 12), Color.black));
			jLocalSettingPanel.add(jLocalPortLabel, gridBagConstraints1);
			jLocalSettingPanel.add(getJLocalPortTextField(), gridBagConstraints);
		}
		return jLocalSettingPanel;
	}

	/**
	 * This method initializes jRemoteSettingPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJRemoteSettingPanel() {
		if (jRemoteSettingPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 3;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.gridy = 2;
			jAuthenticationTypeLabel = new JLabel();
			jAuthenticationTypeLabel.setText("認証方法:");
			jAuthenticationTypeLabel.setPreferredSize(new Dimension(100, 13));
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.gridy = 1;
			jUserNameLabel = new JLabel();
			jUserNameLabel.setText("ユーザ名:");
			jUserNameLabel.setPreferredSize(new Dimension(100, 13));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			jHostAndPortLabel = new JLabel();
			jHostAndPortLabel.setText("ホストとポート:");
			jHostAndPortLabel.setPreferredSize(new Dimension(100, 13));
			jRemoteSettingPanel = new JPanel();
			jRemoteSettingPanel.setLayout(new GridBagLayout());
			jRemoteSettingPanel.setBorder(BorderFactory.createTitledBorder(null, "リモートホストの設定 (SSH)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("MS UI Gothic", Font.PLAIN, 12), Color.black));
			jRemoteSettingPanel.add(jHostAndPortLabel, gridBagConstraints2);
			jRemoteSettingPanel.add(getJHostAndPortPanel(), gridBagConstraints3);
			jRemoteSettingPanel.add(jUserNameLabel, gridBagConstraints4);
			jRemoteSettingPanel.add(getJUserNameTextField(), gridBagConstraints5);
			jRemoteSettingPanel.add(jAuthenticationTypeLabel, gridBagConstraints6);
			jRemoteSettingPanel.add(getJAuthenticationTypePanel(), gridBagConstraints7);
			jRemoteSettingPanel.add(getJAuthenticationOptionPanel(), gridBagConstraints8);
		}
		return jRemoteSettingPanel;
	}

	/**
	 * This method initializes jLocalPortTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJLocalPortTextField() {
		if (jLocalPortTextField == null) {
			jLocalPortTextField = new JTextField();
			jLocalPortTextField.setColumns(10);
		}
		return jLocalPortTextField;
	}

	/**
	 * This method initializes jHostTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJHostTextField() {
		if (jHostTextField == null) {
			jHostTextField = new JTextField();
			jHostTextField.setColumns(20);
		}
		return jHostTextField;
	}

	/**
	 * This method initializes jUserNameTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJUserNameTextField() {
		if (jUserNameTextField == null) {
			jUserNameTextField = new JTextField();
			jUserNameTextField.setColumns(16);
		}
		return jUserNameTextField;
	}

	/**
	 * This method initializes jAuthenticationTypePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthenticationTypePanel() {
		if (jAuthenticationTypePanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			jAuthenticationTypePanel = new JPanel();
			jAuthenticationTypePanel.setLayout(flowLayout);
			jAuthenticationTypePanel.add(getJAuthPassRadioButton(), null);
			jAuthenticationTypePanel.add(getJAuthPublicKeyRadioButton(), null);
		}
		return jAuthenticationTypePanel;
	}

	/**
	 * This method initializes jAuthPassRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJAuthPassRadioButton() {
		if (jAuthPassRadioButton == null) {
			jAuthPassRadioButton = new JRadioButton();
			jAuthPassRadioButton.setAction(new SwitchCardAction(
					getJAuthenticationOptionPanel(),
					getJAuthPasswordPanel().getName()));
			jAuthPassRadioButton.setText("パスワード");
		}
		return jAuthPassRadioButton;
	}

	/**
	 * This method initializes jAuthPublicKeyRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJAuthPublicKeyRadioButton() {
		if (jAuthPublicKeyRadioButton == null) {
			jAuthPublicKeyRadioButton = new JRadioButton();
			jAuthPublicKeyRadioButton.setAction(new SwitchCardAction(
					getJAuthenticationOptionPanel(),
					getJAuthPublicKeyPanel().getName()));
			jAuthPublicKeyRadioButton.setText("公開鍵");
		}
		return jAuthPublicKeyRadioButton;
	}

	/**
	 * This method initializes jAuthenticationOptionPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthenticationOptionPanel() {
		if (jAuthenticationOptionPanel == null) {
			jAuthenticationOptionPanel = new JPanel();
			jAuthenticationOptionPanel.setLayout(new CardLayout());
			jAuthenticationOptionPanel.add(getJAuthPasswordPanel(), getJAuthPasswordPanel().getName());
			jAuthenticationOptionPanel.add(getJAuthPublicKeyPanel(), getJAuthPublicKeyPanel().getName());
		}
		return jAuthenticationOptionPanel;
	}

	/**
	 * This method initializes jAuthPasswordPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthPasswordPanel() {
		if (jAuthPasswordPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.LEFT);
			jAuthPasswordLabel = new JLabel();
			jAuthPasswordLabel.setText("パスワード:");
			jAuthPasswordPanel = new JPanel();
			jAuthPasswordPanel.setLayout(flowLayout1);
			jAuthPasswordPanel.setName("jAuthPasswordPanel");
			jAuthPasswordPanel.add(jAuthPasswordLabel, null);
			jAuthPasswordPanel.add(getJAuthPasswordField(), null);
		}
		return jAuthPasswordPanel;
	}

	/**
	 * This method initializes jAuthPasswordField
	 *
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getJAuthPasswordField() {
		if (jAuthPasswordField == null) {
			jAuthPasswordField = new JPasswordField();
			jAuthPasswordField.setColumns(16);
		}
		return jAuthPasswordField;
	}

	/**
	 * This method initializes jAuthPublicKeyPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthPublicKeyPanel() {
		if (jAuthPublicKeyPanel == null) {
			jAuthPublicKeyLabel = new JLabel();
			jAuthPublicKeyLabel.setText("秘密鍵:");
			jAuthPublicKeyPanel = new JPanel();
			jAuthPublicKeyPanel.setLayout(new BoxLayout(getJAuthPublicKeyPanel(), BoxLayout.Y_AXIS));
			jAuthPublicKeyPanel.setName("jAuthPublicKeyPanel");
			jAuthPublicKeyPanel.add(getJAuthPublicKeyPanel1(), null);
			jAuthPublicKeyPanel.add(getJAuthPublicKeyPanel2(), null);
		}
		return jAuthPublicKeyPanel;
	}

	/**
	 * This method initializes jAuthPublicKeyTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJAuthPublicKeyTextField() {
		if (jAuthPublicKeyTextField == null) {
			jAuthPublicKeyTextField = new JTextField();
			jAuthPublicKeyTextField.setColumns(16);
		}
		return jAuthPublicKeyTextField;
	}

	/**
	 * This method initializes jAuthPublicKeyButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJAuthPublicKeyButton() {
		if (jAuthPublicKeyButton == null) {
			jAuthPublicKeyButton = new JButton();
			jAuthPublicKeyButton.setAction(new OpenAction(this, getJAuthPublicKeyTextField()));
			jAuthPublicKeyButton.setText("選択");
		}
		return jAuthPublicKeyButton;
	}

	/**
	 * This method initializes jOperationPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJOperationPanel() {
		if (jOperationPanel == null) {
			jStatusLabel = new JLabel();
			jStatusLabel.setText("");
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(FlowLayout.RIGHT);
			jOperationPanel = new JPanel();
			jOperationPanel.setLayout(flowLayout3);
			jOperationPanel.add(jStatusLabel, null);
			jOperationPanel.add(getJConnectButton(), null);
			jOperationPanel.add(getJDisconnectButton(), null);
		}
		return jOperationPanel;
	}

	/**
	 * This method initializes jConnectButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJConnectButton() {
		if (jConnectButton == null) {
			jConnectButton = new JButton();
			jConnectButton.setAction(new ConnectAction());
			jConnectButton.setText("接続");
		}
		return jConnectButton;
	}

	/**
	 * This method initializes jDisconnectButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJDisconnectButton() {
		if (jDisconnectButton == null) {
			jDisconnectButton = new JButton();
			jDisconnectButton.setAction(new DisconnectAction());
			jDisconnectButton.setText("切断");
		}
		return jDisconnectButton;
	}

	/**
	 * This method initializes jHostAndPortPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJHostAndPortPanel() {
		if (jHostAndPortPanel == null) {
			jAndLabel = new JLabel();
			jAndLabel.setText(":");
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setAlignment(FlowLayout.LEFT);
			jHostAndPortPanel = new JPanel();
			jHostAndPortPanel.setLayout(flowLayout4);
			jHostAndPortPanel.add(getJHostTextField(), null);
			jHostAndPortPanel.add(jAndLabel, null);
			jHostAndPortPanel.add(getJPortTextField(), null);
		}
		return jHostAndPortPanel;
	}

	/**
	 * This method initializes jPortTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJPortTextField() {
		if (jPortTextField == null) {
			jPortTextField = new JTextField();
			jPortTextField.setColumns(10);
		}
		return jPortTextField;
	}

	/**
	 * This method initializes jAuthPublicKeyPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthPublicKeyPanel1() {
		if (jAuthPublicKeyPanel1 == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(FlowLayout.LEFT);
			jAuthPublicKeyPanel1 = new JPanel();
			jAuthPublicKeyPanel1.setLayout(flowLayout2);
			jAuthPublicKeyPanel1.add(jAuthPublicKeyLabel, null);
			jAuthPublicKeyPanel1.add(getJAuthPublicKeyTextField(), null);
			jAuthPublicKeyPanel1.add(getJAuthPublicKeyButton(), null);
		}
		return jAuthPublicKeyPanel1;
	}

	/**
	 * This method initializes jAuthPublicKeyPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAuthPublicKeyPanel2() {
		if (jAuthPublicKeyPanel2 == null) {
			FlowLayout flowLayout5 = new FlowLayout();
			flowLayout5.setAlignment(FlowLayout.LEFT);
			jAuthPublicKeyPasswordLabel = new JLabel();
			jAuthPublicKeyPasswordLabel.setText("秘密鍵のパスワード (もしあれば):");
			jAuthPublicKeyPanel2 = new JPanel();
			jAuthPublicKeyPanel2.setLayout(flowLayout5);
			jAuthPublicKeyPanel2.add(jAuthPublicKeyPasswordLabel, null);
			jAuthPublicKeyPanel2.add(getJAuthPublicKeyPasswordField(), null);
		}
		return jAuthPublicKeyPanel2;
	}

	/**
	 * This method initializes jAuthPublicKeyPasswordField
	 *
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getJAuthPublicKeyPasswordField() {
		if (jAuthPublicKeyPasswordField == null) {
			jAuthPublicKeyPasswordField = new JPasswordField();
			jAuthPublicKeyPasswordField.setColumns(16);
		}
		return jAuthPublicKeyPasswordField;
	}

}
