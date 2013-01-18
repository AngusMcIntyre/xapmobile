package primayer.android.delta.commands;

import java.text.ParseException;

import primayer.android.delta.fields.Field;

public abstract class DeltaCommand extends Command {
	public final String prefix;
	
	public DeltaCommand(String prefix){
		this.prefix = prefix;
	}
	
	final public boolean parse(CharSequence input) throws CommandParseException, LoggerErrorException{
//		if(this.getClass() != ErrorCommand.class)
//		{
//			ErrorCommand err = new ErrorCommand();
//			if(err.tryParse(input))
//				throw new LoggerErrorException(err);
//		}
		
		if(VerifyInput(input)){
			return onParse(input.toString());
		}
		
		return false;
	}
	
	final public boolean tryParse(CharSequence input)
	{
		try {
			parse(input);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
		
	protected boolean VerifyInput(CharSequence input) {
		if(input.toString().startsWith("d")){
			return input.toString().indexOf(prefix) == 1;
		}
		return false;
	}

	@Override
	public String toString() {
		return toString(false);
	}
	public String toString(boolean changesOnly) {
		StringBuilder fb = new StringBuilder();
		
		for(Field<?> f : super.fields){
			if(f.getValue() != null)
			{
				if(!(!f.hasChanged() && changesOnly))
					fb.append(f.toString());
			}
				
			fb.append(',');
		}
		
		if(fb.length() > 0)
			fb.setLength(fb.length() - 1);
		
		return toQueryString() + "=" + fb.toString();
	}
	
	public String toQueryString(){
		return "#" + prefix;
	}

	protected boolean onParse(String input) throws CommandParseException{

		String[] split = input.substring(prefix.length() + 2).split(",");
		
		//parse all fields that have text
		if(super.fields.size() >= split.length){
			for(int i = 0; i < fields.size(); i++){
				try
				{
					if(!split[i].equals(""))
					{
						if(!(super.fields.get(i).parse(split[i])))
							return false; //if one field fails, fail them all.
					}
				}
				catch(ParseException e)
				{
					throw new CommandParseException(this, i, split[i+1]);
				}
			}
		}
		
		//commit all value in all fields
		for(Field<?> f : super.fields)
			f.commitValue();
		
		return true;
	}
}

