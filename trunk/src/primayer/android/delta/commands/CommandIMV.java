package primayer.android.delta.commands;

import primayer.android.delta.fields.FieldFloat;
import primayer.android.delta.fields.FieldUnit;

public class CommandIMV extends DeltaCommand {
	
	public FieldFloat Digital1AValue = null;
	public FieldUnit Digital1AUnit = null;
	
	public FieldFloat Digital1BValue = null;
	public FieldUnit Digital1BUnit = null;
	
	public boolean Digital1AHasValue = false;
	public boolean Digital1BHasValue = false;
	
	private boolean[] AnalogueChannelHasValue = new boolean[7];
	
	public FieldFloat InternalBatteryVoltage = null;
	public FieldFloat ExternalPowerVoltage = null;
	public FieldFloat GSMSignalStrength = null;
	
	public CommandIMV() {
		super("IMV");
		
		//digital 1a
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		//digital 1b
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		//digital 2a
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		//digital 2b
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		//analog channels
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldUnit());
		
		fields.add(new FieldFloat());
		fields.add(new FieldFloat());
		fields.add(new FieldFloat());
		
		Digital1AValue = (FieldFloat)super.fields.get(0);
		Digital1AUnit = (FieldUnit)super.fields.get(1);
		
		Digital1BValue = (FieldFloat)super.fields.get(2);
		Digital1BUnit = (FieldUnit)super.fields.get(3);
		
		InternalBatteryVoltage = (FieldFloat)super.fields.get(22);
		ExternalPowerVoltage = (FieldFloat)super.fields.get(23);
		GSMSignalStrength = (FieldFloat)super.fields.get(24);
	}
	
	public final FieldFloat getAnalogueValue(int channelNumber){
		int index = (channelNumber * 2) + 6;
		return (FieldFloat)fields.get(index);
	}

	public final FieldUnit getAnalogueUnit(int channelNumber){
		int index = (channelNumber * 2) + 7;
		return (FieldUnit)fields.get(index);
	}
	
	public final boolean hasAnalogueChannelGotValue(int channelNumber){
		if(channelNumber < 1 || channelNumber > 7) return false;
		
		return this.AnalogueChannelHasValue[channelNumber - 1];
	}
	
	@Override
	protected boolean onParse(String input) throws CommandParseException {
		//flag channels as having values based upon text content
		String[] split = input.substring(prefix.length() + 2).split(",");
		
		this.Digital1AHasValue = split[0].trim().length() > 0;
		this.Digital1BHasValue = split[2].trim().length() > 0;
		
		for(int i = 0; i < 7; i++){
			int index = (i * 2) + 8;
			this.AnalogueChannelHasValue[i] = split[index].trim().length() > 0;
		}
		
		return super.onParse(input);
	}
}
