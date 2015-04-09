package WodahBot;


/**
 *
 * @author asher_000
 */
public class WodahBot {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	String channel = args[0];
    	String url = args[1];
    	String username = args[2];
    	String password = args[3];
    	String oauth = args[4];
        Bot bot = new Bot(channel,url,username,password);
        bot.setVerbose(true);
        bot.myConnect(oauth);
                
     
    }
   
}