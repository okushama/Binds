package okushama.binds;



import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatClickData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Console extends GuiScreen
{
    private String field_73898_b = "";

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = 0;
    private boolean field_73897_d = false;
    private boolean field_73905_m = false;
    private int field_73903_n = 0;
    private List field_73904_o = new ArrayList();

    /** used to pass around the URI to various dialogues and to the host os */
    private URI clickedURI = null;

    /** Chat entry field */
    protected ConsoleTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";
    
    public Console(){
    	prevMessages.add("");
    }

    public Console(String par1Str)
    {
        this.defaultInputFieldText = par1Str;
    }
    
  public static HashMap<String, String> help = new HashMap<String, String>();
    
    static{
    	help.put("Help", "You must really need help!");
    }
    
   
    
    public void handleInput(String in){
    	if(in.toLowerCase().startsWith("bind")){
    		if(!in.toLowerCase().contains(" ")){
    			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.RED+"Usage: bind <key> <command/message>");
    			return;
    		}
    		String[] args = in.toLowerCase().split(" ");
    		if(args.length < 3){
    			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.RED+"Usage: bind <key> <command/message>");
    			return;
    		}
    		if(args.length > 2){
	    		String keyToBind = args[1];
	    		String command = in.substring(args[0].length()+args[1].length()+2);
	    		BindsMod.log(keyToBind+" = "+Keyboard.getKeyIndex(keyToBind.toUpperCase()));
	    		KeyBinding[] binds = Minecraft.getMinecraft().gameSettings.keyBindings;
	    		for(KeyBinding bind : binds){
		    		if(Keyboard.getKeyIndex(keyToBind.toUpperCase()) == bind.keyCode){
		    			String loc = LanguageRegistry.instance().getStringLocalization(bind.keyDescription, "en_US");
		    			if(loc.length() == 0) loc = bind.keyDescription;
		    			if(loc.contains(".")){
		    				loc = loc.split("\\.")[1];
		    			}
		    			Minecraft.getMinecraft().thePlayer.addChatMessage("Not binding over the "+loc+" key!");
		    			return;
	
		    		}
	    		}
	    		if(Keyboard.getKeyIndex(keyToBind.toUpperCase()) > 0){
		    		BindsMod.log("Binding \""+command+"\" to "+keyToBind);
	    			Binds.addBind(Keyboard.getKeyIndex(keyToBind.toUpperCase()), command);
	    			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.GREEN+"Bound '"+command+"' to '"+keyToBind.toUpperCase()+"'.");
	    		}
    		}
    	}
    	if(in.toLowerCase().startsWith("unbind")){
    		if(!in.toLowerCase().contains(" ")){
    			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.RED+"Usage: unbind <key>");
    			return;
    		}
    		String[] args = in.toLowerCase().split(" ");
    		if(args.length != 2){
    			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.RED+"Usage: unbind <key>");
    			return;
    		}
    		if(args.length > 1){
	    		String keyToBind = args[1];
	    		if(Keyboard.getKeyIndex(keyToBind.toUpperCase()) > 0){
	    			if(Binds.keyBound(Keyboard.getKeyIndex(keyToBind.toUpperCase()))){
	    				BindsMod.log("Unbinding "+keyToBind);
	    				Binds.removeBind(Keyboard.getKeyIndex(keyToBind.toUpperCase()));
	    				Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.GREEN+"Unbound '"+keyToBind.toUpperCase()+"'.");
	    			}else{
	        			Minecraft.getMinecraft().thePlayer.addChatMessage(EnumChatFormatting.RED+"'"+keyToBind.toUpperCase()+"' is not bound to anything!");
	    			}

	    		}
    		}
    	}
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
	public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = prevMessages.size();
        this.inputField = new ConsoleTextField(this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(34);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
	public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
	protected void keyTyped(char par1, int par2)
    {
        this.field_73905_m = false;

        if (par2 == 15)
        {
            this.completePlayerName();
        }
        else
        {
            this.field_73897_d = false;
        }

        if (par2 == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par2 == 28)
        {
            String var3 = this.inputField.getText().trim();

            if (var3.length() > 0)
            {
                this.handleInput(var3);
                Console.prevMessages.add(var3);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par2 == 200)
        {
            this.getSentHistory(-1);
        }
        else if (par2 == 208)
        {
            this.getSentHistory(1);
        }
        else if (par2 == 201)
        {
            this.mc.ingameGUI.getChatGUI().scroll(19);
        }
        else if (par2 == 209)
        {
            this.mc.ingameGUI.getChatGUI().scroll(-19);
        }
        else
        {
            this.inputField.textboxKeyTyped(par1, par2);
        }
    }

    /**
     * Handles mouse input.
     */
    @Override
	public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0)
        {
            if (var1 > 1)
            {
                var1 = 1;
            }

            if (var1 < -1)
            {
                var1 = -1;
            }

            if (!isShiftKeyDown())
            {
                var1 *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(var1);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
	protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0 && this.mc.gameSettings.chatLinks)
        {
            ChatClickData var4 = this.mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());

            if (var4 != null)
            {
                URI var5 = var4.getURI();
            }
        }

        this.inputField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    @Override
	public void confirmClicked(boolean par1, int par2)
    {
        if (par2 == 0)
        {
            if (par1)
            {
                this.func_73896_a(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void func_73896_a(URI par1URI)
    {
        try
        {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {par1URI});
        }
        catch (Throwable var4)
        {
            var4.printStackTrace();
        }
    }

    /**
     * Autocompletes player name
     */
    public void completePlayerName()
    {
        String var3;

        if (this.field_73897_d)
        {
            this.inputField.deleteFromCursor(this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.field_73903_n >= this.field_73904_o.size())
            {
                this.field_73903_n = 0;
            }
        }
        else
        {
            int var1 = this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false);
            this.field_73904_o.clear();
            this.field_73903_n = 0;
            String var2 = this.inputField.getText().substring(var1).toLowerCase();
            var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.func_73893_a(var3, var2);

            if (this.field_73904_o.isEmpty())
            {
                return;
            }

            this.field_73897_d = true;
            this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
        }

        if (this.field_73904_o.size() > 1)
        {
            StringBuilder var4 = new StringBuilder();

            for (Iterator var5 = this.field_73904_o.iterator(); var5.hasNext(); var4.append(var3))
            {
                var3 = (String)var5.next();

                if (var4.length() > 0)
                {
                    var4.append(", ");
                }
            }

            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var4.toString(), 1);
        }

        this.inputField.writeText((String)this.field_73904_o.get(this.field_73903_n++));
    }

    private void func_73893_a(String par1Str, String par2Str)
    {
        if (par1Str.length() >= 1)
        {
            //this.mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
            this.field_73905_m = true;
        }
    }
    
    public static ArrayList<String> prevMessages = new ArrayList<String>();

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int par1)
    {
        int var2 = this.sentHistoryCursor + par1;
        int var3 = prevMessages.size();

        if (var2 < 0)
        {
            var2 = 0;
        }

        if (var2 > var3)
        {
            var2 = var3;
        }

        if (var2 != this.sentHistoryCursor)
        {
            if (var2 == var3)
            {
                this.sentHistoryCursor = var3;
                this.inputField.setText(this.field_73898_b);
            }
            else
            {
                if (this.sentHistoryCursor == var3)
                {
                    this.field_73898_b = this.inputField.getText();
                }

                this.inputField.setText(Console.prevMessages.get(var2));
                this.sentHistoryCursor = var2;
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
    	GL11.glPushMatrix();
    	GL11.glTranslatef((width/2)-(width/4), -30F, 0F);
        drawRect(2, this.height - 14, this.width/2 - 2, this.height - 2, 0x44440077);
        this.inputField.drawTextBox();
        super.drawScreen(par1, par2, par3);
        GL11.glPopMatrix();
    }

    public void func_73894_a(String[] par1ArrayOfStr)
    {
        if (this.field_73905_m)
        {
            this.field_73904_o.clear();
            String[] var2 = par1ArrayOfStr;
            int var3 = par1ArrayOfStr.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                String var5 = var2[var4];

                if (var5.length() > 0)
                {
                    this.field_73904_o.add(var5);
                }
            }

            if (this.field_73904_o.size() > 0)
            {
                this.field_73897_d = true;
                this.completePlayerName();
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }
}
