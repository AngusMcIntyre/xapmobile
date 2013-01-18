package primayer.android.delta.commands;

import primayer.android.delta.fields.FieldInteger;
import primayer.android.delta.fields.FieldString;

public class ErrorCommand extends DeltaCommand {

	final FieldInteger code;
	final FieldString command;
	final FieldInteger character;
	
	public ErrorCommand() {
		super("ERROR");
		
		fields.add(new FieldInteger());
		fields.add(new FieldString());
		fields.add(new FieldInteger());
		
		code = (FieldInteger)fields.get(0);
		command = (FieldString)fields.get(1);
		character = (FieldInteger)fields.get(2);
	}

}
