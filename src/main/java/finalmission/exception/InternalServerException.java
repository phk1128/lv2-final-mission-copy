package finalmission.exception;

public class InternalServerException extends GeneralException{

    public InternalServerException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
