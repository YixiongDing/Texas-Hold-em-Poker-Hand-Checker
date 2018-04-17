/* Author: Yixiong Ding <yixiongd@student.unimelb.edu.au> *
*  Date: 30 September, 2017          					  *
*  The University of Melbourne                            */

/* Import Pattern and Matcher libraries for the use of Regular expression */
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/* The main program	*/
public class Poker 
{
	
	/* Declaration of constants */
	static final private int MAXIMUMCARDS = 5;
	static final private String TEN = "T";
	static final private String JACK = "J";
	static final private String QUEEN = "Q";
	static final private String KING = "K";
	static final private String ACE = "A";
	static final private int STRAIGHTFLUSH = 9;
	static final private int FOUROFAKIND = 8;
	static final private int FULLHOUSE = 7;
	static final private int FLUSH = 6;
	static final private int STRAIGHT = 5;
	static final private int THREEOFAKIND = 4;
	static final private int TWOPAIR = 3;
	static final private int ONEPAIR = 2;
	static final private int HIGHCARD = 1;
	
	/* This method is used to classify the poker card as its rank and suit for each card in the hand for all players */
	private static void classifyData(Player[] players, int totalplayers, String[] data)
	{	
		int p_num = 0; /* player number, denotes the ranking of the player */
		int c_num = 0; /* card number, denotes the ranking of card in data array */
		int p_c_num = 0; /* player card number, denotes the ranking of card in player's hand */
		
		/* This loop is used to collect and classify card information from “data” array and put into "players" array */
		for(p_num=0; p_num<totalplayers; p_num++)
		{
			players[p_num] = new Player(p_num+1);
	
			for(p_c_num=0, c_num=p_num*MAXIMUMCARDS; c_num<(p_num*MAXIMUMCARDS+MAXIMUMCARDS) 
					&& p_c_num<MAXIMUMCARDS; c_num++, p_c_num++)
			{

				/* Take the first char as the rank, and the second as the suit of the card */
				String rank_temp = data[c_num].substring(0,1).toUpperCase();
				players[p_num].handcards_suit[p_c_num] = data[c_num].substring(1,2).toUpperCase();	
				
				/* Convert the rank from char to integer, and store it into the array */
				switch(rank_temp)
				{
					case TEN:
						players[p_num].handcards_rank[p_c_num] = 10;
						break;
					case JACK:
						players[p_num].handcards_rank[p_c_num] = 11;
						break;
					case QUEEN:
						players[p_num].handcards_rank[p_c_num] = 12;
						break;
					case KING:
						players[p_num].handcards_rank[p_c_num] = 13;
						break;
					case ACE:
						players[p_num].handcards_rank[p_c_num] = 14;
						break;
					default:	
						players[p_num].handcards_rank[p_c_num] = Integer.parseInt(rank_temp);
						break;
				}
			}
			
			/* Sort the rank array in ascending order */
			java.util.Arrays.sort(players[p_num].handcards_rank);
		}
	}
	
	/* This method is used to check the possible classification of the poker hand for all players */
	private static void checkClassification(Player[] players, int totalplayers)
	{	
		for(int p_num=0; p_num<totalplayers; p_num++)
		{
			/* Check if a poker hand is Straight, Flush or Straight flush */
			checkStraightFlush(players[p_num]);
			
			/* Check if a poker hand is Four of a kind, Full house, Three of a kind, Two pair or One pair */
			checkPair(players[p_num]);
			
			/* If the poker hand is not the one of the 8 classifications, then it is High card */
			if(players[p_num].getClassification()==0) updateInfo(players[p_num], HIGHCARD, players[p_num].handcards_rank[4], 0);
			
			/* Print the classification of the poker hand */
			System.out.printf("Player %d: %s\n", players[p_num].getPlayerNum(), players[p_num].getDescription());
		}	
	}
	
	/* This method is used to check if a poker hand is Straight, Flush or Straight flush */
	private static void checkStraightFlush(Player player)
	{
		int rank_temp = player.handcards_rank[0];
		String suit_temp = player.handcards_suit[0];
		int rank_counter = 0;
		int suit_counter = 0;
		
		/* This loop is used to check if the poker hand is Straight */
		for(int i=1; i<MAXIMUMCARDS; i++)
		{
			
			/* If the rank of next card is larger than this card's rank by 1, increase the counter and test the next card */
			if((rank_temp + 1) == player.handcards_rank[i])
			{
				rank_temp = player.handcards_rank[i];
				rank_counter++;
			}
		}
		
		/* This loop is used to check if the poker hand is Flush */
		for(int i=0; i<MAXIMUMCARDS; i++)
		{
			
			/* If the suit of next card is the same as this card's suit, increase the counter and test the next card */
			if(player.handcards_suit[i].equals(suit_temp)) 
			{
				suit_counter++;	
			}
		}
		
		/* Based on the results of the two counters, update the poker hand information of the player */
		if(rank_counter == 4 && STRAIGHT > player.getClassification()) updateInfo(player, STRAIGHT, player.handcards_rank[4],0);
		if(suit_counter == 5 && FLUSH > player.getClassification()) updateInfo(player, FLUSH, player.handcards_rank[4],0);	
		if(rank_counter == 4 && suit_counter == 5) updateInfo(player, STRAIGHTFLUSH, player.handcards_rank[4],0);
	}
	
	/* This method is used to check if a poker hand is Four of a kind, Full house, Three of a kind, Two pair or One pair */
	private static void checkPair(Player player)
	{
		
		/* Take the middle rank as the reference rank for kind can avoid the 
		 * smallest or largest rank is different, i.e. 1,6,6,6,6 or 6,6,6,6,9 */
		int kind_temp = player.handcards_rank[2];
		
		/* Take the second and fourth rank as the reference rank for pair1 and pair2 can avoid the 
		 * smallest, middle or largest rank is single, i.e. 1,2,2,3,3 or 2,2,3,4,4 or 2,2,3,3,4 */
		int pair1_temp = player.handcards_rank[1];
		int pair2_temp = player.handcards_rank[3];
		int kind_counter = 0;
		int pair1_counter = 0;
		int pair2_counter = 0;
		
		/* This loop is used to count how many pairs a poker hand has and how many cards have the same rank */
		for(int i=0; i<MAXIMUMCARDS; i++)
		{
			if(player.handcards_rank[i] == kind_temp) kind_counter++;
			if(player.handcards_rank[i] == pair1_temp) pair1_counter++;
			if(player.handcards_rank[i] == pair2_temp) pair2_counter++;
		}		
		
		/* Based on the results of the three counters, update the poker hand information of the player */
		if(kind_counter == 4 && FOUROFAKIND > player.getClassification()) updateInfo(player, FOUROFAKIND, kind_temp,0);
		if(((pair1_counter == 3 && pair2_counter == 2) 
				|| (pair1_counter == 2 && pair2_counter == 3)) && FULLHOUSE > player.getClassification())
		{
			if(pair1_counter == 3) updateInfo(player, FULLHOUSE, pair1_temp,pair2_temp);
			if(pair2_counter == 3) updateInfo(player, FULLHOUSE, pair2_temp,pair1_temp);
		}
		if(kind_counter == 3 && THREEOFAKIND > player.getClassification()) updateInfo(player, THREEOFAKIND, kind_temp,0);
		if(pair1_counter == 2 && pair2_counter == 2  && TWOPAIR > player.getClassification()) updateInfo(player, TWOPAIR, pair2_temp, pair1_temp);
		if(((pair1_counter == 2) || (pair2_counter == 2)) && ONEPAIR > player.getClassification())
		{
			if(pair1_counter == 2) updateInfo(player, ONEPAIR, pair1_temp, 0);
			if(pair2_counter == 2) updateInfo(player, ONEPAIR, pair2_temp, 0);
		}
	}
	
	/* This method is used to update the poker hand information of the player, i.e r1, r2 and description */
	private static void updateInfo(Player player, int classification, int r1, int r2) 
	{
		
		/* Based on the classification of the poker hand, update r1, r2 and description */
		switch(classification)
		{
		case STRAIGHTFLUSH:
			player.updateClassification(STRAIGHTFLUSH);
			player.updateR1(r1);
			player.updateDescription(player.getStringR1()+"-high straight flush");
			break;
		case FOUROFAKIND:
			player.updateClassification(FOUROFAKIND);
			player.updateR1(r1);
			player.updateDescription("Four "+player.getStringR1()+"s");
			break;
		case FULLHOUSE:
			player.updateClassification(FULLHOUSE);
			player.updateR1(r1);
			player.updateR2(r2);
			player.updateDescription(player.getStringR1()+"s full of "+player.getStringR2()+"s");
			break;
		case FLUSH:
			player.updateClassification(FLUSH);
			player.updateR1(r1);
			player.updateDescription(player.getStringR1()+"-high flush");
			break;
		case STRAIGHT:
			player.updateClassification(STRAIGHT);
			player.updateR1(r1);
			player.updateDescription(player.getStringR1()+"-high straight");
			break;
		case THREEOFAKIND:
			player.updateClassification(THREEOFAKIND);
			player.updateR1(r1);
			player.updateDescription("Three "+player.getStringR1()+"s");
			break;
		case TWOPAIR:
			player.updateClassification(TWOPAIR);
			player.updateR1(r1);
			player.updateR2(r2);
			player.updateDescription(player.getStringR1()+"s over "+player.getStringR2()+"s");
			break;
		case ONEPAIR:
			player.updateClassification(ONEPAIR);
			player.updateR1(r1);
			player.updateDescription("Pair of "+player.getStringR1()+"s");
			break;
		default:
			player.updateClassification(HIGHCARD);
			player.updateR1(r1);
			player.updateDescription(player.getStringR1()+"-high");
			break;
		}
	}
	
	/* This method is used to print out the game result */
	private static void printResult(Player[] players, int totalplayers)
	{
		int[] winners = new int[totalplayers];
		int winnernum = 0; /* winner number, denotes the number of winners */
		
		/* Use decideWinner method to choose the winners of the game */
		winnernum = decideWinner(players, totalplayers, winners, winnernum);
		
		/* Based on the player number and winner number to choose the printing format*/
		if(totalplayers == 1) System.exit(1);
		else if(winnernum == 1) System.out.print("Player "+winners[0]+" wins."+"\n");
		else
		{
			System.out.print("Players");
			for(int i=0; i<winnernum-2; i++) System.out.print(" "+winners[i]+",");
			System.out.print(" "+winners[winnernum-2]);
			System.out.print(" and ");
			System.out.print(winners[winnernum-1]+" ");
			System.out.print("draw."+"\n");
		}
	}
	
	/* This method is used to choose the winners of the game */
	private static int decideWinner(Player[] players, int totalplayers, int[] winners, int winnernum)
	{
		int highestmark = 0; /* highest mark, denotes the highest classification of all poker hands */
		int highestpnum = 0; /* highest player number, denotes the the number of player with the highest mark */
		int highestr1 = 0; /* highest r1, denotes the current highest r1 rank in the highest poker hand */
	
		/* This loop is used to find the player whose poker hand has the highest mark */
		for(int i=0; i<totalplayers; i++) 
		{
			
			/* Either the classification is the highest of all players, 
			 * or r1 is the highest among the players with the same classification */
			if(players[i].getClassification() > highestmark 
					|| ((players[i].getClassification() == highestmark) && (players[i].getIntR1()>highestr1))) 
			{
				highestmark = players[i].getClassification();
				highestpnum = i;
				highestr1 = players[i].getIntR1();
				winners[0] = players[i].getPlayerNum();
				winnernum = 1;
			}
		}
		
		/* Check the poker hand	of other players with the same classification in detail, 
		 * if the highest mark is Straight flush and Straight */
		if(highestmark == STRAIGHTFLUSH || highestmark == STRAIGHT)
		{
			for(int i=0; i<totalplayers; i++) 
			{
				if((players[i].getClassification() == highestmark) && (i != highestpnum))
				{
					
					/* If the r1 of current poker hand is larger than the highest r1, change the winner */
					if(players[i].getIntR1() > highestr1) {
						winners[0] = players[i].getPlayerNum();
						winnernum = 1;
						highestr1 = players[i].getIntR1();
					}
					
					/* If the r1 of current poker hand is the same as the highest r1, another winner is found */
					else if(players[i].getIntR1() == highestr1)
					{
						winners[winnernum] = players[i].getPlayerNum();
						winnernum++;
					}
				}
			}	
		}
		
		/* Check the poker hand	of other players with the same classification and r1 in detail,
		 * if the highest mark is one of the other 7 classifications */
		else
		{
			for(int i=0; i<totalplayers; i++) 
			{
				if((players[i].getClassification() == highestmark) && (i != highestpnum) && players[i].getIntR1() == highestr1)
				{
					boolean found = false;
					
					/* This loop is used to check if there are any ranks of current poker hand 
					 * is higher than the current winner's poker hand */
					for(int j=MAXIMUMCARDS-1; j>-1; j--)
					{
						
						/* If any larger rank is found, change the winner */
						if(players[i].handcards_rank[j] > players[highestpnum].handcards_rank[j] 
								&& players[i].handcards_rank[j] != highestr1)
						{	
							winners[0] = players[i].getPlayerNum();
							winnernum = 1;
							highestr1 = players[i].getIntR1();
							highestpnum = i;
							found = true;
							break;
						}
						
						/* If any smaller rank is found, denotes the player of this poker hand cannot be the winner, break the loop */
						else if(players[i].handcards_rank[j] < players[highestpnum].handcards_rank[j] 
								&& players[i].handcards_rank[j] != highestr1)
						{	
							found = true;
							break;
						}
					}
					
					/* Neither larger nor smaller rank is found, denotes another winner is found */
					if(!found)
					{
						winners[winnernum] = players[i].getPlayerNum();
						winnernum++;
					}
				}
			}
		}
		return winnernum;
	}
	
	/* The main method of the program */
	public static void main(String[] args)
	{	
		int totalplayers = args.length/MAXIMUMCARDS; /* The number of total players */
		String[] data = new String[args.length]; /* The array of all cards */
		Player[] players = new Player[totalplayers]; /* The array of all players */
		Pattern pattern = Pattern.compile("^[2-9aAtTjJqQkK][cCdDhHsS]$"); /* The regular expression of valid card names */

		/* Check if the number of arguments is valid */
		if(args.length % MAXIMUMCARDS !=0 || args.length == 0)
		{
			System.out.print("Error: wrong number of arguments; must be a multiple of "+MAXIMUMCARDS+"\n");
			System.exit(0);
		}
		
		/* Check if the card name is valid by using regular expression */
		for(int i=0; i<args.length; i++) 
		{	
			Matcher matcher = pattern.matcher(args[i]);
			
			/* If the card name is valid, put into the data array */
			if(matcher.find()) data[i] = args[i];
			else
			{
				System.out.print("Error: invalid card name "+"'" +args[i]+ "'"+"\n");     
				System.exit(0);
			}
		}	
		
		/* Classify the poker card as its rank and suit for each card in the poker hand for all players */
		classifyData(players, totalplayers, data);
		
		/* Check the possible classification of the poker hand for all players */
		checkClassification(players, totalplayers);
		
		 /* Print out the game result */
		printResult(players, totalplayers);
	}
}