package primayer.android.delta.commands;

public class CommandACAL extends DeltaCommand {
	public final String ChannelIndex;
	public CommandACAL(String channelIndex) {
		super("ACAL" + channelIndex);
		
		ChannelIndex = channelIndex;
	}
}
