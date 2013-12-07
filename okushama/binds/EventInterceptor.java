package okushama.binds;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;

public class EventInterceptor {
	
	@ForgeSubscribe
	public void onChatServer(ServerChatEvent evt){
		if(evt.player.worldObj.isRemote)
		BindsMod.log(evt.username+" "+evt.username);
	}
	
	@ForgeSubscribe
	public void onChatClient(ClientChatReceivedEvent evt){
		
	}
	

}
