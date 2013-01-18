package primayer.android.delta.fields;

import java.text.ParseException;

public class FieldString extends Field<String> {

	public FieldString(){
		super("");
	}
	@Override
	protected String onParse(String input) throws ParseException {
		return input;
	}

}
