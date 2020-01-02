package pt.iscte.pidesco.snippets.service;

public class ServiceOperationResult
{
    public boolean isSuccess;
    public String errorMessage;

    public ServiceOperationResult(boolean isSuccess, String errorMessage) {
		this.isSuccess = isSuccess;
		this.errorMessage = errorMessage;
	}
	
	public static ServiceOperationResult Success()
    {
        return new ServiceOperationResult(true, null);
    }

    public static ServiceOperationResult Failure(String errors)
    {
    	return new ServiceOperationResult(false, errors);
    }
}