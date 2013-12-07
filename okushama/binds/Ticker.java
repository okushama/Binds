package okushama.binds;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Ticker implements ITickHandler{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.CLIENT))){
			Binds.tick();
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "Binds Mod Ticker";
	}

}
