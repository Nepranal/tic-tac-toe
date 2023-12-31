import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * GUI part for the client
 * 
 * @author Abel Haris Harsono
 *
 */
public class ClientGui {
	JFrame frame = new JFrame("Tic-Tac-Toe");
	JButton submitButton = new JButton("Submit");
	JTextField nameInputTextField = new JTextField(20);
	JPanel boardPanel = new JPanel();
	JMenuBar menu = new JMenuBar();
	JMenu control = new JMenu("Control");
	JMenuItem exit = new JMenuItem("Exit");
	JMenu help = new JMenu("Help");
	JMenuItem instruction = new JMenuItem("Instruction");
	JLabel topMessageLabel = new JLabel();
	JButton[] buttons = new JButton[9];
	String symbol;

	String cmdServer = "";
	String cmdClient = "";

	Socket socket;
	PrintWriter writer;
	BufferedReader reader;

	/**
	 * Initializes the swing components for the game
	 */
	public void go() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel inputPanel = new JPanel();
		inputPanel.add(nameInputTextField);
		inputPanel.add(submitButton);
		submitButton.setEnabled(false);
		nameInputTextField.setEnabled(false);

		control.add(exit);
		help.add(instruction);
		menu.add(control);
		menu.add(help);
		frame.setJMenuBar(menu);

		topMessageLabel.setText("Enter your player name...");

		boardPanel.setLayout(new GridBagLayout());
		// TODO Add listener to the buttons
		for (int i = 0; i < 9; ++i) {
			buttons[i] = new JButton("");
			buttons[i].setEnabled(false);
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.fill = GridBagConstraints.BOTH;
		int counter = 0;
		for (int i = 0; i < 3; ++i) {
			c.gridy = i;
			for (int j = 0; j < 3; ++j) {
				c.gridx = j;
				boardPanel.add(buttons[counter], c);
				counter++;
			}
		}

		frame.add(topMessageLabel, BorderLayout.NORTH);
		frame.add(inputPanel, BorderLayout.SOUTH);
		frame.add(boardPanel, BorderLayout.CENTER);

		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	/**
	 * Returns the a JButton object of the submit button
	 */
	public JButton getSubmitButton() {
		return submitButton;
	}

	/**
	 * Returns the exit menu Item object
	 */
	public JMenuItem getExitMenuItem() {
		return exit;
	}

	/**
	 * Returns the Intructions menu item object
	 */
	public JMenuItem getInstructionMenuItem() {
		return instruction;
	}

	/**
	 * Set whether submit button is enabled or disabled
	 * 
	 * @param b If b==true, submit button is enabled. Otherwise, disabled
	 */
	public void setEnableSubmitButton(boolean b) {
		submitButton.setEnabled(b);
	}

	/**
	 * Set whether name input text field is enabled or disabled
	 * 
	 * @param b If b==true, text field is enabled. Otherwise, disabled
	 */
	public void setEnableTextField(boolean b) {
		nameInputTextField.setEnabled(b);
	}

	/**
	 * Set the message displayed to the player at the top as 'message'
	 * 
	 * @param message
	 */
	public void setTopMessageLabel(String message) {
		topMessageLabel.setText(message);
	}

	/**
	 * Set the message displayed to the player at the top. Any message would append
	 * the player's name so: "{message}+" "+{playerName}"
	 * 
	 * @param message
	 */
	public void setInitialMessageLabel(String message) {
		topMessageLabel.setText(message + " " + nameInputTextField.getText());
	}

	/**
	 * Enables the buttons in the board to be pressed
	 */
	public void enableBoard() {
		for (int i = 0; i < 9; ++i) {
			buttons[i].setEnabled(true);
		}
	}

	/**
	 * Update the board with moves made
	 * 
	 * @param s
	 */
	public void updateBoard(String s) {
		char symbol = s.charAt(2);
		int pos = Integer.parseInt(s.charAt(0) + "");
		buttons[pos].setText(symbol + "");
	}

	/**
	 * Set the symbol "s" to be used by the player
	 * 
	 * @param s
	 */
	public void setSymbol(String s) {
		symbol = s;
	}

	/**
	 * Get the symbol used by the player
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Get the buttons array of the board
	 */
	public JButton[] getButtons() {
		return buttons;
	}

	/**
	 * Show warning message "s".
	 * 
	 * @param s
	 */
	public void showWarningMessage(String s) {
		JOptionPane.showMessageDialog(frame, s, "Message", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Disable the board
	 */
	public void disableBoard() {
		for (int i = 0; i < 9; ++i) {
			buttons[i].setEnabled(false);
		}
	}

	/**
	 * Update the button at position pos with the correct symbol
	 * 
	 * @param symbol
	 * @param pos
	 */
	public void updateButton(String symbol, int pos) {
		buttons[pos].setText(symbol);
	}

	/**
	 * Sets title of the frame as "message"
	 * 
	 * @param message
	 */
	public void setFrameMessage(String message) {
		frame.setTitle(message + " " + nameInputTextField.getText());
	}

	/**
	 * Returns the player's name
	 */
	public String getNameInputTextField() {
		return nameInputTextField.getText();
	}
}
