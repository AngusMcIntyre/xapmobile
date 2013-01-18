package primayer.android.delta.fields;

import java.text.ParseException;

import primayer.android.delta.data.Unit;

public class FieldUnit extends Field<Unit> {

	public FieldUnit(){
		super(Unit.Unknown);
	}
	
	@Override
	public String toString() {
		return Integer.toString(super.getValue().index);
	}

	@Override
	protected Unit onParse(String input) throws ParseException {
		try {
			return Unit.fromInteger(Integer.parseInt(input));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage(), 0);
		}	
	}

}
