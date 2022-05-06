package five.utility.exception;

public enum OperationExceptionType {
    ERR_FORBIDDEN,
    ERR_INTERNAL,
    ERR_NOT_AUTHORIZED,
    ERR_ACC_ILLEGAL_STATUS,
    ERR_ACC_OR_PASS,
    ERR_ACC_TOKEN,
    ERR_ACC_NOT_EXIST,
//    Mandatory field contains null
    ERR_MISS_MANDATORY_FIELD,
//  Phone number format wrong
    ERR_ILLEGAL_PHONE,
//  Email format wrong
    ERR_ILLEGAL_EMAIL,
//  No one role selected
    ERR_NO_ROLES,
    ERR_MAX_UPLOAD_SIZE_EXCEEDED,
//  Not allowed to do this
    ERR_NOT_ALLOWED
}
