/**
 * This class is the blueprint for the board in the game
 * @author Abel Haris Harsono
 *
 */
public class Board {
	String[] table = new String[9];

	/**
	 * Non-default constructor for the board. Initializes the board with an empty string
	 */
	public Board() {
		for (int i = 0; i < 9; ++i) {
			table[i] = "";
		}
	}

	/**
	 * Add move(symbol of the player) to the correct position 'pos'
	 * @param move
	 * @param pos
	 */
	public void add(String move, int pos) {
		table[pos] = move;
	}
	
	/**
	 * Check for game over. 
	 * @return Return true if yes
	 */
	public boolean gameOver() {
		if (vertical() || horizontal() || diagonal()) {
			return true;
		}
		return false;
	}

	private boolean vertical() {
		String symbol;
		boolean flag;
		for (int i = 0; i < 3; ++i) {
			flag = true;
			symbol = table[i];
			for (int j = i; j < i + 7; j += 3) {
				if (!table[j].equals(symbol))
					flag = false;
			}

			if (flag == true && !symbol.equals("")) {
				return flag;
			}
		}
		return false;
	}

	private boolean horizontal() {
		String symbol;
		boolean flag;
		for (int i = 0; i < 7; i += 3) {
			System.out.println(i);
			flag = true;
			symbol = table[i];
			for (int j = i; j < i + 3; ++j) {
				if (!table[j].equals(symbol))
					flag = false;
			}

			if 	(flag == true && !symbol.equals(""))
				return flag;
		}
		return false;
	}

	private boolean diagonal() {
		if (diagonal(0) || diagonal(2)) {
			return true;
		}
		return false;
	}

	private boolean diagonal(int index) {
		String symbol = table[index];
		for (int i = 4; i < 2*4-index+1; i+=4-index) {
			if(!table[i].equals(symbol)) return false;
		}
		if(!symbol.equals(""))
		return true;
		return false;
	}

	/**
	 * Used to print the board to console
	 */
	public void print() {
		for(int i=0;i<9;++i) {
			if(table[i].equals("")) System.out.print("#");
			System.out.print(table[i]+" ");
			if((i+1)%3==0) System.out.println("");
		}
	}
	
	/**
	 * Check is the table is full. Return true if so
	 */
	public boolean full() {
		int counter=0;
		for(int i=0;i<9;++i) {
			if(!table[i].equals("")) counter++;
		}
		if(counter==9) {
			return true;
		}
		return false;
	}

	
	/**
	 * Main function, used to test the class
	 * @param args, unused
	 */
	public static void main(String[] args) {
		Board board = new Board();

		board.add("X", 8);
		board.add("X", 7);
		board.add("X", 6);
		board.print();
		if(board.gameOver())System.out.println("Bruh");
	}
}
