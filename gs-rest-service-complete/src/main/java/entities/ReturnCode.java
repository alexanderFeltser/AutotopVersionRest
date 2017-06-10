package entities;

public class ReturnCode {
	private int errorCode;
	private String errorMessage;
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

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
