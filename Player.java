/* Author: Yixiong Ding <yixiongd@student.unimelb.edu.au> *
*  Date: 30 September, 2017          					  *
*  The University of Melbourne                            */

public class Player {
	
	/* Declaration of constants and variables */
	static final private int MAXIMUMCARDS = 5;
	public int[] handcards_rank = new int[MAXIMUMCARDS]; /* The array of all ranks of the cards of the poker hand */
	public String[] handcards_suit = new String[MAXIMUMCARDS]; /* The array of all suits of the cards of the poker hand */
	private int playernum; /* Player number */
	private int classification = 0; 
	private int r1;
	private int r2;
	private String description; /* Stores the information about the poker hand that needs to be printed out */
	
	/* Constructor, create a player with the specified player number */
	public Player(int playernum)
	{
		this.playernum = playernum;
	}
	
	/* Returns the player number of the player */
	public int getPlayerNum()
	{
		return this.playernum;
	}
	
	/* Modify the poker hand's classification of the player to the specified classification */
	public void updateClassification(int classification)
	{
		this.classification = classification;
	}
	
	/* Returns the classification of the player's poker hand */
	public int getClassification()
	{
		return this.classification;
	}
	
	/* Modify the poker hand's r1 of the player to the specified rank */
	public void updateR1(int r1)
	{
		
		this.r1 = r1;
	}
	
	/* Modify the poker hand's r2 of the player to the specified rank */
	public void updateR2(int r2)
	{
		this.r2 = r2;
	}
	
	/* Returns the r1 of the player's poker hand as integer */
	public int getIntR1()
	{
		return this.r1;
	}
	
	/* Returns the r2 of the player's poker hand as integer */
	public int getIntR2()
	{
		return this.r2;
	}
	
	/* Returns the r1 of the player's poker hand as string */
	public String getStringR1()
	{
		String R1;
		switch(r1)
		{
		case 11:
			R1 = "Jack";
			break;
		case 12:
			R1 = "Queen";
			break;
		case 13:
			R1 = "King";
			break;
		case 14:
			R1 = "Ace";
			break;
		default:
			R1 = Integer.toString(this.r1);
			break;
		}
		return R1;
	}
	
	/* Returns the r2 of the player's poker hand as string */
	public String getStringR2()
	{
		String R2;
		switch(r2)
		{
		case 11:
			R2 = "Jack";
			break;
		case 12:
			R2 = "Queen";
			break;
		case 13:
			R2 = "King";
			break;
		case 14:
			R2 = "Ace";
			break;
		default:
			R2 = Integer.toString(this.r2);
			break;
		}
		return R2;
	}
	
	/* Modify the poker hand's description of the player to the specified description */
	public void updateDescription(String description)
	{
		this.description = description;
	}

	/* Returns the description of the player's poker hand */
	public String getDescription()
	{
		return this.description;
	}
}
