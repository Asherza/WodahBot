package WodahBot;

import java.util.ArrayList;



public class BettingGame {
	private int limitplayers = 0;
	private int activePlayers = 0;
	private int rounds;
	private String ref;
	private boolean inprogress = false;
	private boolean roundstate;

	mySQL server = null;
	private ArrayList<BetGamePlayer> players = new ArrayList<BetGamePlayer>();
	//Constructor for ref limit of players and rounds to play
	public BettingGame(String refp, int limit,int round,mySQL serv){
		server = serv;
		limitplayers = limit;
		rounds = round;
		ref = refp;
	}
	//Constructor for 
	public BettingGame(String refp,int limit)
	{
		limitplayers = limit;
		ref = refp;
		rounds = 5;
	}
	public BettingGame(String refp)
	{
		ref = refp;
		rounds = 5;
		limitplayers = 5;
	}
	public BettingGame(String refp,double round)
	{
		ref = refp;
		rounds = (int) round;
		limitplayers = 5;
	
	}
	public String getRef()
	{
		return ref;
	}
	public String toString()
	{
		return "Referee= " + ref + " Player Limit = " + limitplayers + " Points to win = " + rounds + " Players: " + getPlayersNames()  ;
	}
	public String getPlayersNames(){
		String list = "";
		for(BetGamePlayer x:players){
			list += x.getName() + ":" + x.getpoints() + " ";
		}
		return list;
	}
	public String addPlayer(String user){
		if(!inprogress){
			return "Please start a game first!";
		}
		else
		{
			if(activePlayers < limitplayers)
			{
				activePlayers++;
				if(search(user) == null){
					players.add(new BetGamePlayer(user,server));
					return user + " Has been added to the game!";
				}
				else
				{
					return user + " Is already in the game!";
						
				}
				
			}
			else
			{
				return "The game is full!";
			}
		}
		//return null;
	}
	public String startGame()
	{
		if(inprogress)
		{
			return "End the round first!";
		}
		else
		{
			inprogress = true;
			
		}
		
		return null;
	}
	public String setBet(String usern,int bet)
	
	{
		if(search(usern)== null){
			return usern + " Is not in this game use ~join to join!";
		}
		else
		{
			BetGamePlayer user = search(usern);
			//Bet if returns 0 then bet is placed if 1 then bet was already placed
			boolean x = user.Bet(bet);
			
			System.out.println(x);
			if(x)
			{	
				return usern + " Has already placed a guess of " + user.getBet();
			}
			else
			{
				
				for(int i = 0;i<= players.size()-1;i++)
				{
					if(usern.equals(players.get(i).getName())){
						players.remove(i);
						break;
					}
				}
				players.add(user);
				return usern + "'s guess is:" + user.getBet();
			}
			
		}
		
		
		}
	public String startRound()
	{
		if(roundstate)
		{
			return "Please end round";
		}
		else
		{
			roundstate = true;
			return "Round has started place your guess!";
			
		}
	}
	public String endRound(int real)
	{
		String winners = "";
		boolean winTrip = false;
		for(BetGamePlayer x:players){
			if(x.getBet() == real){
				winners += x.getName() + " ";
				x.reset(true);
				System.out.println(winners);
				if(x.getpoints() == rounds){
					winTrip = true;
				}
			}
			else if (x.getBet() == 0){
				x.softReset();
			}
			else
			{
				x.reset(false);
			}
		}
		System.out.println(winners);
		roundstate = false;
		String k = "The winners are: " + winners;
		
		System.out.println(k);
		if(winTrip)
		{
			return endGame();
		}
		else
		{
			return k;
		}
		
	}
	public String endGame()
	{
		for(BetGamePlayer x:players){
			if(x.getpoints() == rounds){
				x.giveWin();
		}
		}
		sortPlayers();
		String winnermoney = giveMoney();
		save();
		return "~done Winners and money: " + winnermoney;
	}
	public String giveMoney()
	{
			String listplayer = "";
			int prev = 0;
			int nextgive = 100;
			int gives = 0;
			boolean firstcheck = false;
			for(int i = players.size()-1; i>=0;i--){
				if(players.get(i).getpoints() > 0){
					if(!firstcheck){
						firstcheck = true;
								players.get(i).setMoney(players.get(i).getpoints() * 100); 
								prev = players.get(i).getpoints();	
								listplayer += players.get(i).getName() + ": " + (players.get(i).getpoints() * 100) + " ";
								gives++;
					}
					else
					{
						if (players.size() > 6 && gives < 3) {
							if(players.get(i).getpoints() < prev)
							{
								nextgive /=2;
								prev = players.get(i).getpoints();
								players.get(i).setMoney(players.get(i).getpoints() * nextgive); 
								listplayer += players.get(i).getName() + ": " + (players.get(i).getpoints() * 100) + " ";
								gives++;
								continue;
								
							}
							else
							{
								players.get(i).setMoney(players.get(i).getpoints() * nextgive); 
								listplayer += players.get(i).getName() + ": " + (players.get(i).getpoints() * 100) + " ";
								gives++;
								continue;
							}
							
						}
					
						else
						{
							if(players.get(i).getpoints() == prev){
								players.get(i).setMoney(prev * nextgive);
								listplayer += players.get(i).getName() + ": " + (players.get(i).getpoints() * 100) + " ";
							}
							else{
								break;
						
							}
						}
					}
				}
			}
				
			for(int i = 0;i <= players.size() -1;i++){
				System.out.println(players.get(i).getMoney());
			}
			return listplayer;
		}
		
	
	public void sortPlayers(){
		/*For testing 
		 * Random xs = new Random();
		int t = 0;
		while (t < 7){
			players.add(new BetGamePlayer(xs.nextInt(10)));
			t++;
			
		}
		*/
		for(int i = 0;i <= players.size()- 1;i++)
		{
			for(int j = 1;j<= players.size()- 1;j++)
			{
				BetGamePlayer hold1 = players.get(j-1);
				BetGamePlayer hold2 = players.get(j);
				
				if(hold1.getpoints() > hold2.getpoints())
				{
					players.set(j-1, hold2);
					players.set(j, hold1);
				}
				
			}
		}
		for(BetGamePlayer x :players){
			System.out.println(x.getpoints());
		}

	}
	public String endGameManual()
	{
		int wins = 0;
		for(BetGamePlayer x:players){
			if(x.getpoints() > wins){
				wins = x.getpoints();
			}
		}
		for(BetGamePlayer x:players){
			if(x.getpoints() == wins && x.getpoints() > 0){
				x.giveWin();
			}
		}
		sortPlayers();
		String x =giveMoney();
		save();
		return "Game Has been ended winners/money: " + x;
	}
	public String getScores(){
		String scores = "Scores: ";
		for(BetGamePlayer x:players){
			scores += " " + x.getName() + ": " + x.getpoints();
			}
			return scores;
	}
	public String resetRound(){
		for(BetGamePlayer x:players){
			x.softReset();
		}
		return "Round reset!";
	}
	public boolean getRoundState()
	{
		return roundstate;
				
	}
	public BetGamePlayer search(String user)
	{
		for(BetGamePlayer x: players){
   		 if(user.equals(x.getName())){
   			 return x;
   		 }
   	 }
   	 return null;
	}
	public void save(){
		for(BetGamePlayer user: players)
		{
			server.insert("REPLACE INTO guessgame(id,username,wins,money) VALUES ("
					+ user.getId()
					+ ",'"
					+ user.getName()
					+ "',"
					+ user.getWins()
					+","
					+ user.getMoney()
					+ ")");
					
		}
		
	}
	public String removePlayer(String user){
		int index = 0;
		for(BetGamePlayer x: players){
			if(user.equals(x.getName()))
			{
				players.remove(index);
				return "Player removed";
			}
			index++;
		}
		return "Player was not found!";
	}
	public String switchRef(String refs){
		ref = refs;
		return refs + " is the new ref!";
	}
	
	
}
