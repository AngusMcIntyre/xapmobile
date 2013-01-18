package primayer.android.delta.fields;

import java.text.ParseException;

public class FieldInteger extends Field<Integer> {

	public FieldInteger(){
		super(1);
	}
	@Override
	protected Integer onParse(String input) throws ParseException {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage(), 0);
		}
	}
}