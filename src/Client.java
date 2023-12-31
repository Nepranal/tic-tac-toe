
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

import javax.swing.*;

/**
 * Client side of the game. Run once per player
 * 
 * @author Abel Haris Harsono
 *
 */
public class Client {
	ClientGui gui = new ClientGui();
	Board board = new Board();

	Socket socket;
	PrintWriter writer;
	BufferedReader reader;

	/**
	 * Main function of the class. Used to start the client
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Client().go();
	}

	/**
	 * Initialize the gui and connection. Also starts the thread to listen for input
	 * from the server
	 */
	public void go() {
		gui.go();
		connectGui();
		setUpConnection();

		Runnable job1 = new ServerHandler();
		Thread thread = new Thread(job1);
		thread.start();
	}

	private void connectGui() {
		JButton[] buttons = gui.getButtons();
		gui.getSubmitButton().addActionListener(new SubmitButtonListener());
		gui.getExitMenuItem().addActionListener(new ExitButtonListener());
		gui.getInstructionMenuItem().addActionListener(new HelpMenuListener());
		for (int i = 0; i < 9; ++i) {
			buttons[i].addActionListener(new BoardListener());
		}
	}

	private void setUpConnection() {
		try {
			socket = new Socket("127.0.0.1", 5000);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Listener class for the submit button
	 * 
	 * @author Abel Haris Harsono
	 *
	 */
	public class SubmitButtonListener implements ActionListener {

		/**
		 * Once the submit button is clicked and the name input box is not empty, it
		 * will disable itself, disable the input box, rename title and messages and
		 * also notify the server
		 */
		public void actionPerformed(ActionEvent e) {
			if (!gui.getNameInputTextField().equals("")) {
				gui.setEnableSubmitButton(false);
				gui.setEnableTextField(false);
				gui.setInitialMessageLabel("WELCOME");
				gui.setFrameMessage("Tic Tac Toe-Player: ");
				writer.println("Named");
				writer.flush();
			}
		}

	}

	/**
	 * Listener class for the exit menu item
	 * 
	 * @author Abel Haris Harsono
	 *
	 */
	public class ExitButtonListener implements ActionListener {

		/**
		 * Once clicked, close the game
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);

		}

	}

	/**
	 * Listener class for the instruction menu item
	 * 
	 * @author Abel Haris Harsono
	 *
	 */
	public class HelpMenuListener implements ActionListener {

		/**
		 * Displays the rules and tips for the game
		 */
		public void actionPerformed(ActionEvent e) {
			gui.showWarningMessage("Some information about the game:\n" + "Criteria for a valid move:\n"
					+ "-The move is not occupied by any mark.\n" + "-The move is made in the player's turn.\n"
					+ "-The move is made within the 3 x 3 board.\n"
					+ "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n"
					+ "-Player 1 wins\n" + "-Player 2 wins.\n" + "-Draw.");

		}

	}

	/**
	 * Listener for all 9 buttons of the boards.
	 * 
	 * @author Abel Haris Harsono
	 *
	 */
	public class BoardListener implements ActionListener {

		/**
		 * Once any button is pressed, first check if the button is filled or not. If
		 * not, notify the server about the move and update the button and board
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton button = (JButton) e.getSource();
			if (button.getText().equals("")) {
				int index = searchIndex(button);
				writer.println(index + " " + gui.getSymbol());
				writer.flush();
				gui.updateBoard(index + " " + gui.getSymbol());
				gui.setTopMessageLabel("Valid move, wait for your opponent.");
			}
		}

		private int searchIndex(JButton button) {
			JButton[] buttons = gui.getButtons();
			for (int i = 0; i < 9; ++i) {
				if (buttons[i] == button)
					return i;
			}
			return -1;
		}

	}

	/**
	 * Class for handling messages from the server. Implements the runnable class
	 * for multi-threading
	 * 
	 * @author Abel Haris Harsono
	 *
	 */
	public class ServerHandler implements Runnable {

		@Override
		/**
		 * Once run, will accept messages from the server
		 */
		public void run() {
			try {
				String input;
				while ((input = reader.readLine()) != null) {
					System.out.println("Command received: " + input);
					if (input.equals("Start")) {
						gui.setEnableSubmitButton(true);
						gui.setEnableTextField(true);
					}

					else if ((input.equals("X") || input.equals("O")))
						gui.setSymbol(input);
					else if (input.equals("Enable"))
						gui.enableBoard();
					else if (input.equals("Disable"))
						gui.disableBoard();
					else if (input.length() == 3 && !input.equals("Win")) {
						board.add(input.charAt(2) + "", Integer.parseInt(input.charAt(0) + ""));
						gui.updateButton(input.charAt(2) + " ", Integer.parseInt(input.charAt(0) + ""));
						if (!(input.charAt(2) + "").equals(gui.getSymbol()))
							gui.setTopMessageLabel("Your opponent has moved, now is your turn");
					} else if (input.equals("Win")) {
						gui.showWarningMessage("Congratulations. You win.");
						gui.disableBoard();
						break;
					} else if (input.equals("Lose")) {
						gui.showWarningMessage("You lose.");
						gui.disableBoard();
						break;
					} else if (input.equals("Draw")) {
						gui.showWarningMessage("Draw");
						gui.disableBoard();
						break;
					}

					else if (input.equals("Disconnect")) {
						gui.showWarningMessage("Game ends. One of the player left");
						gui.disableBoard();
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
