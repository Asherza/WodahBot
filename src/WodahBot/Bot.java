package WodahBot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.jibble.pircbot.*;

import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

/**
 *
 * @author asher_000
 */
public class Bot extends PircBot {
	String[] ops = { "wodahsr","jarvitz", "unitedwesin", "zfg1", "bluntslide42",
			"cosmowright", "shibbypod", "xtbdmx", "adarax", "grokken",
			"giuocob", "swishzors", "cafde", "cfox7", "elminster45",
			"runnerguy2489", "antayn", "poodleskirt", "headshots57",
			"qwerty1605", "ishmon", "kosmicd12", "twiii_", "gold3nnova",
			"flubbermage", "rossy__", "kongolthedragoon", "zeldajunkie",
			"happysnowman", "admiralsmit", "asherza", "herooftermina",
			"alexwanderson", "gregortixlkyns", "lortwogo", "taylortotftw",
			"ixblaze", "ellomenop", "pinkfloydftw", "koobes", "grimm_wolfe",
			"comeasur337", "rwhitegoose", "qweczol", "illudude",
			"goldenjimbo007", "natalyahasdied", "jbop1626", "heatedinacup",
			"karljobst", "inkosidevinyl", "perfectace", "davidclemensn",
			"thebigbossman007", "meleo_", "austinthegamer", "wodahbot",
			"kochson", "mirrormage1", "scottplays", "homeonice222",
			"peeinacan", "sniper_patrol", "wyst3r", "ryanlockwood",
			"hitekaimerr", "stoxenbawns", "overfiendvip", "swissrollz",
			"hotshott41187", "sweetnumb", "lastframemineswitch", "itsjustindt",
			"kolombo_", "dsxchallenger", "hotgrills6969", "theeblight",
			"hugphoenix", "icyspeedruns", "niirokitsune", "vgsbrandon1",
			"mouserscribe", "wodahbot_" };
	mySQL server ;
	BettingGame game = null;
	ArrayList<BetGamePlayer> Betplayers = new ArrayList<BetGamePlayer>();
	ArrayList<Player> players = new ArrayList<Player>();
	String owner ;
	UserAgent userAgent = new UserAgent();

	public Bot(String chan,String url,String username, String pass)

	{
		owner = chan;
		server = new mySQL(url,username,pass);
		this.setName("WodahBot_");
		this.setLogin("WodahBot_");
	}

	public void myConnect(String oauth) {

		try {
			connect("irc.twitch.tv", 6667,
					 oauth);
			joinChannel(owner);

		} catch (Exception x) {
			System.out.println(x);
		}
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {

		join(login);
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String mes = message.toLowerCase();
		System.out.println(mes);
		// HELLO COMMAND
		if (mes.startsWith("~hello")) {
			sendMessage(owner, "Hello " + sender + "!");
		}
		// M8 COMMAND
		else if (mes.startsWith("~m8")) {
			User[] users = this.getUsers(owner);
			Random rng = new Random();
			int rando = rng.nextInt(rng.nextInt(users.length));
			sendMessage(owner, sender + "'s m8 is " + users[rando].getNick());
		}
		// RECENT PR COMMAND
		else if (mes.startsWith("~recentpr")) {
			String messager = recentpr();
			sendMessage(owner, messager);
		}
		else if (mes.startsWith("~commands")){
			sendMessage(owner, "http://pastebin.com/cRZxEtjk");
		}
		// Game commands
		else if (mes.startsWith("~game.listPlayers")) {
			if (players.size() == 0) {
				sendMessage(owner, "No one is playing!");
			} 
			else {
				String list = "Players: ";
				for (Player users : players) {
					list += " " + users.getUsername() + ",";
				}
				sendMessage(owner, list);
			}
		} 
		else if (mes.startsWith("~game.attack")) {

		}
		else if (mes.startsWith("~game.myinfo")) {
			Player user = search(sender);
			sendMessage(owner,
					"ID: " + user.getId() + " Username: " + user.getUsername()
							+ " Level: " + user.getLevel() + " HitPoints: "
							+ user.getCurHp());

		} 
		else if (mes.startsWith("~game.debug.join")) {
			String messager = join(login);
			sendMessage(owner, messager);
		}

		else if (mes.startsWith("~game.debug.takedmg")) {
			Player user = search(sender);
			user.takeDps();
			sendMessage(owner, "Current HitPoints: " + user.getCurHp());
		} 
		else if (mes.startsWith("~game.debug.save")) {
			
		}
		// BettingGame commands
		// Starting game put ~BetGame.start (rounds,players)
		else if (mes.startsWith("~guessgame.start")) {
			
			if(isOp(login)){
				int start = mes.indexOf(("("));
				int end = mes.indexOf(",");
				// Finds if rounds has a value or not
				String rounds = mes.substring(start + 1, end);
				start = end;
				end = mes.indexOf(")");
				String playerCount = mes.substring(start + 1, end);
				game = new BettingGame(sender, Integer.parseInt(rounds),
						Integer.parseInt(playerCount), server);

				sendMessage(owner, game.toString());
				game.startGame();
			}
			else{
				sendMessage(owner, "Must be a Mod to start a game!");
			}
			
		}
		else if (mes.startsWith("~guessgame.round.start")) {

			if (game == null) {
				sendMessage(owner, "You must start a game!");
			} 
			else {
				if (sender.equals(game.getRef())) {

					sendMessage(owner, game.startRound());
				} 
				else {
					sendMessage(owner, "The Ref must start the round!");
				}
			}
		}
		else if (mes.startsWith("~guessgame.round.end")) {
			if (game == null) {
				sendMessage(owner, "Start the game first!");
			} else {
				if (game.getRoundState()) {
					int start = mes.indexOf(" ");
					String end = mes.substring(start + 1);
					int ans = Integer.parseInt(end);
					if (sender.equals(game.getRef())) {
						String isDone = game.endRound(ans); //CALLED FROM HERE!!!!!!
						if (isDone.startsWith("~done")) {
							String weDone = isDone.substring(isDone
									.indexOf(" ") + 1);
							sendMessage(owner, weDone);
							game = null;
						} 
						else {
							sendMessage(owner, isDone);
							sendMessage(owner, game.getScores());
						}

					} 
					else {
						sendMessage(owner, "Only " + game.getRef()
								+ " Can end the round!");
					}
				}
				else {
					sendMessage(owner, "Please start the round!");
				}
			}
		} 
		else if (mes.startsWith("~guessgame.round.reset")) {
			if (game == null) {
				sendMessage(owner, "Start the game first!");
			} else {
				if (game.getRef().equals(sender)) {
					if (game.getRoundState()) {
						sendMessage(owner, game.resetRound());
					} 
					else {
						sendMessage(owner,
								"The round was not even started Kappa");
					}
				} 
				else {
					sendMessage(owner, "Only the Ref can reset the round!");

				}
			}
		} 
		else if (mes.startsWith(("~join"))) {
			if (game == null) {
				sendMessage(owner, "Start a game first!");
			} 
			else {
				sendMessage(owner, game.addPlayer(sender));
			}

		}
		
		else if (mes.startsWith("~guessgame.myinfo")){
			
			sendMessage(owner, getGuessData(sender));
		}
		
		else if (mes.startsWith("~guessgame.kick")){
			if(game == null)
			{
				sendMessage(owner, "Start a game first!");
			}
			else
			{
				if(sender.equals(game.getRef())){
					int start = mes.indexOf(" ");
					String player = mes.substring(start + 1);
					sendMessage(owner,game.removePlayer(player));
				}
				
			}
		}
		else if (mes.startsWith("~guessgame.info"))
		{
			if(game == null){
				sendMessage(owner, "Start a game first!");
			}
			else
			{
				sendMessage(owner, game.toString());
			}
		}
		else if (mes.startsWith("~guessgame.setref"))
		{
			if(game == null)
			{
				sendMessage(owner, "Start a game first!");
			}
			else
			{
				if(sender.equals(game.getRef())){
					User[] users = this.getUsers(owner);
					int start = mes.indexOf(" ");
					String player = mes.substring(start + 1);
					for(User x:users){
						if(x.getNick().equals(player)){
							if(isOp(x.getNick()))
							{
								sendMessage(owner,game.switchRef(x.getNick()));
							}
							else
							{
								sendMessage(owner, "New ref must me a mod!");
							}
						}
					}
				}
				
			}
		}
		else if (mes.startsWith("~guessgame.endgame"))
		{
			if(game == null)
			{
				sendMessage(owner,"Start a game first!");
			}
			else
			{
				if(sender.equals(game.getRef()) || sender.equals("wodahsr"))
				{
					sendMessage(owner,game.endGameManual());
					game = null;
				}
			}
		}
		else if (mes.startsWith("~guessgame.debug.sort")){
			game.sortPlayers();
		}
		else if (mes.startsWith("~guess")) {
			int start = mes.indexOf(" ");
			String bet = mes.substring(start + 1);
			int bets = Integer.parseInt(bet);
			if (game == null) {
				sendMessage(owner, "Start a game first!");
			} 
			else {
				if (game.getRoundState()) {
					String response = game.setBet(sender, bets);
					sendMessage(owner, response);
				}
				else {
					sendMessage(owner, "Round hasn't Started!");
				}
			}
		} 
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {
		for (int i = 0; i <= players.size() - 1; i++) {
			if (login.equals(players.get(i).getUsername())) {
				//save(login); FIX
				players.remove(i);
				break;
			}
		}

	}

	// Creates a recent PR text and returns it
	private String recentpr() {
		String recent = "";
		try {
			userAgent
					.visit("http://rankings.the-elite.net/~Wodahs-Reklaw/goldeneye/history");
			Table table = userAgent.doc.getTable("<table>");
			// Date of recent PR
			Element element = table.getCell(0, 1);
			String date = element.innerHTML();
			// Stage of recent PR
			element = table.getCell(1, 1);
			String stage = element.innerHTML();
			// Finds the difficulty of recent PR
			element = table.getCell(2, 1);
			String diff = element.innerHTML();
			// gets time of recent PR
			element = table.getCell(3, 1);
			String time = element.innerText();
			// create a link to the recent PR
			element = table.getCell(6, 1);
			String hasvid = element.innerText();
			System.out.println(hasvid);
			// checks if theres a video if yes then give link else print Kappa
			if (hasvid.equals("Yes")) {
				String link = element.innerHTML();
				int start = link.indexOf("'");
				int end = link.indexOf("'", start + 1);
				String vid = link.substring(start + 1, end);
				// creates message for bot to send
				recent = "Most Recent PR -- " + stage + " " + diff + ": "
						+ time + ", on " + date + " "
						+ "http://rankings.the-elite.net" + vid;
			} else {

				recent = "Most Recent PR -- " + stage + " " + diff + ": "
						+ time + ", on " + date + " " + "Kappa ";
			}

		} catch (JauntException e) {
			System.out.println(e);
		}
		return recent;
	}

	// Adds a Player to the active players game
	private String join(String usern) {
		ResultSet rs = server.select("SELECT * FROM player WHERE username = '"
				+ usern + "'");
		String result = null;
		try {
			while (rs.next()) {
				result = rs.getString(2);
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		if (result == null) {
			server.insert("INSERT INTO player(username,level,hp,curhp) VALUES ('"
					+ usern + "',1,100,100)");
		}
		for (Player users : players) {
			if (usern.equals(users.getUsername())) {
				return "Player already in the game!";
			}
		}
		rs = server.select("SELECT * FROM player WHERE username = '" + usern
				+ "'");
		int level = 0;
		int hp = 0;
		int curhp = 0;
		int id = 0;
		try {
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
				level = Integer.parseInt(rs.getString(3));
				hp = Integer.parseInt(rs.getString(4));
				curhp = Integer.parseInt(rs.getString(5));
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}
		Player user = new Player(id, usern, level, hp, curhp,server);
		players.add(user);
		
		
		 rs = server.select("SELECT * FROM guessgame WHERE username = '"
				+ usern + "'");
		 result = null;
		try {
			while (rs.next()) {
				result = rs.getString(2);
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		if (result == null) {
			server.insert("INSERT INTO guessgame(username,wins,money) VALUES ('"
					+ usern + "',0,0)");
		}
		return "Player added!";
		
	}

	private Player search(String user) {
		for (Player x : players) {
			if (user.equals(x.getUsername())) {
				return x;
			}
		}
		return null;
	}

	private boolean isOp(String username) {
		for (String user : ops) {
			if (username.equals(user)) {
				return true;
			}
		}
		return false;
	}
	private String getGuessData(String username){
		ResultSet rs = server.select("SELECT * FROM guessgame WHERE username = '" + username
				+ "'");

		int wins = 0;
		int money = 0;
		try {
			while (rs.next()) {
				
				wins = Integer.parseInt(rs.getString(3));
				money = Integer.parseInt(rs.getString(4));
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}
		return username + " Wins: " + wins + " Money: " + money;
	
	}
}
    
