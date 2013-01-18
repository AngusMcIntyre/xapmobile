package primayer.android.delta.commands;

public class LoggerErrorException extends Exception {

	ErrorCommand mErr;
	public LoggerErrorException(ErrorCommand errorCom){
		mErr = errorCom;
	}
	@Override
	public String getMessage() {
		return String.format("A %s error occured in a %s command at character %s", mErr.code, mErr.command, mErr.character);
	}
}
