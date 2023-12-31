
import java.net.*;
import java.util.ArrayList;
import java.io.*;
/**
 * This is the Server part of the game. Run this before the start of any game
 * @author Abel Haris Harsono
 */
public class Server {
	ServerSocket serverSocket;
	Socket socket;
	ArrayList<Socket> sockets = new ArrayList<Socket>();
	ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	ArrayList<Thread> threads = new ArrayList<Thread>();
	Board board = new Board();
	int playerCounter = 0;
	int playerNameCounter = 0;

	/**
	 * Main function to start the server
	 * @param args, Unused
	 */
	public static void main(String[] args) {
		new Server().go();
	}

	private void go() {
		try {
			serverSocket = new ServerSocket(5000);
			while (playerCounter < 2) {
				socket = serverSocket.accept();
				playerCounter++;
				writers.add(new PrintWriter(socket.getOutputStream()));
				sockets.add(socket);
				Runnable clientJob = new ClientHandler(socket, playerCounter);
				Thread thread = new Thread(clientJob);
				threads.add(thread);
				System.out.println("Connected with client " + (playerCounter));
			}
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void giveSymbol() throws IOException {
		int size = writers.size();
		for (int i = 0; i < size; ++i) {
			if (i == 0) {
				writers.get(i).println("X");
				writers.get(i).flush();
				System.out.println("Sent X to 1st client");
			} else if (i == 1) {
				writers.get(i).println("O");
				writers.get(i).flush();
				System.out.println("Sent O to 2nd client");
			}
		}
	}

	private void start() throws IOException {
		giveSymbol();

		writers.get(0).println("Start");
		writers.get(0).flush();
		writers.get(1).println("Start");
		writers.get(1).flush();
		System.out.println("Client started");
		for (int i = 0; i < playerCounter; ++i) {
			threads.get(i).start();
		}
	}

	/**
	 * Inner class for handling the client. The class implements runnable interface to do multi-threading
	 * @author Abel Haris Harsono
	 *
	 */
	public class ClientHandler implements Runnable {
		Socket socket;
		PrintWriter writer;
		int playerNum;

		/**
		 * Non-default constructor for the handler. 
		 * @param s
		 * @param n
		 */
		public ClientHandler(Socket s, int n) {
			socket = s;
			playerNum = n;
		}
		
		/**
		 * Overridden method from the runnable interface. This is used to listen for input coming for the server
		 * ,broadcast any moves made, and declare win/lose/draw status of the game to the correct player.
		 */
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				writer = new PrintWriter(socket.getOutputStream());

				String input;

				while ((input = reader.readLine()) != null) {
					System.out.println("Message received: " + input);
					if (input.equals("Named")) {
						playerNameCounter++;
						if (playerNameCounter == 2) {
							writers.get(0).println("Enable");
							writers.get(0).flush();
						}
					}

					else {
						board.add(input.charAt(2) + "", Integer.parseInt(input.charAt(0) + ""));
						sendMoveMessage(input);

						if (board.gameOver()) {
							writer.println("Win");
							writer.flush();
							writers.get(3 - playerNum - 1).println("Lose");
							writers.get(3 - playerNum - 1).flush();
							break;
						} else if (board.full()) {
							for (PrintWriter writer : writers) {
								writer.println("Draw");
								writer.flush();
							}
							break;
						}

						writer.println("Disable");
						writer.flush();
						writers.get(3 - playerNum - 1).println("Enable");
						writers.get(3 - playerNum - 1).flush();
					}

				}

			} catch (IOException e) {
				writers.get(3 - playerNum - 1).println("Disconnect");
				writers.get(3 - playerNum - 1).flush();
			}
		}

		private void sendMoveMessage(String move) {
			for (PrintWriter writer : writers) {
				writer.println(move);
				writer.flush();
			}
		}
	}
}
