package pt.iscte.pidesco.snippets.service;

public class ServiceOperationResult<T>
{
    public boolean isSuccess;
    public T objectResult;
    public String errorMessage;

    public ServiceOperationResult(boolean isSuccess, T objectResult, String errorMessage) {
		this.isSuccess = isSuccess;
		this.objectResult = objectResult;
		this.errorMessage = errorMessage;
	}
	
	public static <T> ServiceOperationResult<T> Success(T objectResult)
    {
        return new ServiceOperationResult<T>(true, objectResult, null);
    }

    public static <T> ServiceOperationResult<T> Failure(String errors)
    {
    	return new ServiceOperationResult<T>(false, null, errors);
    }
}