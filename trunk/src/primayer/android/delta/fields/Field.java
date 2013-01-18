package primayer.android.delta.fields;

import java.text.ParseException;

public abstract class Field<T> {

	private T mOriginalValue;
	private T mValue;
	
	public Field(T startingValue){
		setValue(startingValue);
		commitValue();
	}
	
	@Override
	public String toString() {
		return mValue.toString();
	}
	
	public T getValue(){
		return mValue;
	}
	public void setValue(T value) {
		this.mValue = value;
	}
	
	final public void commitValue(){
		mOriginalValue = mValue;
	}
	
	/**
	 * @return true if the field has not been changed
	 */
	final public  boolean hasChanged(){
		if(mValue != null)
			return !mValue.equals(mOriginalValue);
		else if(mOriginalValue != null)
			return !mOriginalValue.equals(mValue);
		else return false;
	}

	final public boolean parse(String input) throws ParseException{
		setValue(onParse(input));
		return true;
	}
	protected T onParse(String input) throws ParseException{
		throw new ParseException("Failed to parse field: " + this.getClass().toString() + " with value " + input, 0);
	}
}