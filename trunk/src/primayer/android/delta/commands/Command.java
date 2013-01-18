package primayer.android.delta.commands;

import java.util.ArrayList;

import primayer.android.delta.fields.Field;

public abstract class Command {
	protected ArrayList<Field<?>> fields = new ArrayList<Field<?>>();
	
	public Field<?>[] getFields(){
		return fields.toArray(new Field<?>[fields.size()]);
	}
}
