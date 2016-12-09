package player_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import views.Game;

public class player_view {
	Game gameView;
	String action = "Nothing";
	String actionValue = "";
	ExecutorService clientReceive = Executors.newSingleThreadExecutor();
	
	
	public player_view(Game test) {
		gameView = test;
		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
		threadExecutor.execute(new UpdateData()); //Update client
	}
	
	public void pushButton( ) {
		action = "pressButton";
		actionValue = "1";
		gameView.button.setEnabled(false);
	}
	
	class UpdateData implements Runnable {

		@Override
		public void run() {
			JSONObject ClientData = new JSONObject();
			while(true){
				
				try {Thread.sleep(1000);} catch (InterruptedException e) {} 
				
				ClientData.put("action", action);
				ClientData.put("actionValue", actionValue);
				TCP.clientModule.sendMessage(ClientData.toString());
				
				String ServerData = TCP.clientModule.readServerMessage();
				JSONObject messageJSON = new JSONObject(ServerData); // �নJSON
				
				String action = messageJSON.get("action").toString();
				 if(!action.equals("Nothing")){
					 String actionValue = messageJSON.get("actionValue").toString();
					 if(action.equals("enableButton")){
						 if(actionValue.equals("1")){
							 gameView.button.setEnabled(true);
						 }
					 }
				 }
				player_view.this.action = "Nothing";
			}
		}
		
	}
}
