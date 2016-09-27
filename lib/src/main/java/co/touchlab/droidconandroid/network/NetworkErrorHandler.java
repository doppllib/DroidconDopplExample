package co.touchlab.droidconandroid.network;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kgalligan on 7/29/14.
 */
public class NetworkErrorHandler implements ErrorHandler
{
    @Override
    public Throwable handleError(RetrofitError cause)
    {
        if (cause.isNetworkError())
        {
            return new NetworkException(cause.getCause());
        }

        if(cause.getResponse() != null)
        {
            ResponseException responseException = parseResponse(cause.getResponse());
            if(responseException != null)
                return responseException;
        }

        return new FatalUnknownException(cause.getCause());
    }

    protected ResponseException parseResponse(Response response)
    {
        if(response.getStatus() == 404)
            return new NotFoundException();
        return null;
    }

    public static class NetworkException extends Exception
    {
        public NetworkException()
        {
            super();
        }

        public NetworkException(String detailMessage)
        {
            super(detailMessage);
        }

        public NetworkException(String detailMessage, Throwable throwable)
        {
            super(detailMessage, throwable);
        }

        public NetworkException(Throwable throwable)
        {
            super(throwable);
        }
    }

    public static class ResponseException extends Exception
    {
        public ResponseException()
        {
        }

        public ResponseException(String detailMessage)
        {
            super(detailMessage);
        }

        public ResponseException(String detailMessage, Throwable throwable)
        {
            super(detailMessage, throwable);
        }

        public ResponseException(Throwable throwable)
        {
            super(throwable);
        }
    }

    public static class NotFoundException extends ResponseException
    {}

    public static class FatalUnknownException extends RuntimeException
    {
        public FatalUnknownException()
        {
        }

        public FatalUnknownException(String detailMessage)
        {
            super(detailMessage);
        }

        public FatalUnknownException(String detailMessage, Throwable throwable)
        {
            super(detailMessage, throwable);
        }

        public FatalUnknownException(Throwable throwable)
        {
            super(throwable);
        }
    }

}
