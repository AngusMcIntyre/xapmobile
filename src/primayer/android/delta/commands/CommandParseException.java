package primayer.android.delta.commands;

public class CommandParseException extends Exception {

	final DeltaCommand command;
	final int fieldIndex;
	final String fieldContent;
	
	public CommandParseException(DeltaCommand command, int fieldIndex, String fieldContent)
	{
		this.command = command;
		this.fieldIndex = fieldIndex;
		this.fieldContent = fieldContent;
	}

	@Override
	public String getMessage() {
		return String.format("Failed to parse command %s. Error occured at field %s while trying to parse %s",
				command.prefix,
				fieldIndex,
				fieldContent);
	}
}
