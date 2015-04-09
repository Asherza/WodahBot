package WodahBot;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BetGamePlayer {
	private String name;
	private int id;
	private int points;
	private int bet;
	private boolean hasBet;
	private int wins;
	private int money;
	mySQL server;
	public BetGamePlayer(String nameo,mySQL server){
		name = nameo;
		points = 0;
		hasBet = false;
		this.server = server;
		ResultSet rs = server.select("SELECT * FROM guessgame WHERE username = '" + nameo
				+ "'");
		try {
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
				wins = Integer.parseInt(rs.getString(3));
				money = Integer.parseInt(rs.getString(4));
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}
	public BetGamePlayer(int point){
		points = point;
		id = 0;
		wins = 0;
		money = 0;
		hasBet = false;
	}
	public String getName()
	{
		return name;
	}
	public void addPoint()
	{
		points++;
	}
	public void subPoint()
	{
		points--;
	}
	public void addMoney(int val){
		money += val;
	}
	public int getpoints()
	{
		return points;
	}
	public boolean Bet(int betp)
	{
		System.out.println(hasBet);
		if(hasBet)
		{
			return hasBet;
		}
		else{
			bet = betp;
			hasBet = true;
			return false;
		}
		
	}
	
	public int getBet()
	{
		return bet;
	}
	public int getMoney(){
		return money;
	}
	public void reset(boolean points)
	{
		hasBet = false;
		bet = 0;
		if(points){
			addPoint();
		}
		else{
			subPoint();
		}
	}
	
	public void softReset()
	{
		hasBet = false;
		bet = 0;
	}
	public boolean hasBetcheck(){
		return hasBet;
	}
	public int getId(){
		return id;
	}
	public void giveWin(){
		wins++;
	}
	public int getWins(){
		return wins;
	}
	public void setMoney(int val){
		money += val;
	}
	
	
}
