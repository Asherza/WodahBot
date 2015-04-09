package WodahBot;


public class Player {
	mySQL server = null;
	private String username;
	private int id;
	private int level;
	private int hp;
	private int curhp;
	public Player(int idp,String user,int levelp,int hpp,int curhpp,mySQL serv){
		server = serv;
		id = idp;
		username = user;
		level = levelp;
		hp = hpp;
		curhp = curhpp;
	}
	public String getUsername(){
    	 return username;
     }
	
	public int getLevel(){
		return level;
	}
	public int getHp(){
		return hp;
	}
	public int getCurHp(){
		return curhp;
	}
	public int getId(){
		return id;
	}
	public void takeDps(){
		curhp -= 10;
	}
}
