package primayer.android.delta.fields;

import java.text.ParseException;

public class FieldFloat extends Field<Float> {

	public FieldFloat()
	{
		super(0.0f);
	}
	
	@Override
	protected Float onParse(String input) throws ParseException {
		try {
			return Float.parseFloat(input);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage(), 0);
		}
	}
}