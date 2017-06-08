package entities;

public class ReturnCode {
	private int errorCode;
	private String errorMessage;

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ReturnCode(int errorCode, String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isError() {
		return errorCode != 0;
	}

}
