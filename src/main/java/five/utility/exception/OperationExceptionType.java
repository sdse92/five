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
    ERR_NOT_ALLOWED,
//  Course not exist
    ERR_COURSE_NOT_EXIST,
//  File with this name exist
    ERR_EXIST_FILENAME,
//  File upload error
    ERR_UPLOAD_FILE,
//  Course files upload directory not exist
    ERR_COURSE_UPLOAD_DIRECTORY,
//  File descriptor not exist
    ERR_FILE_DESCRIPTOR_NOT_EXIST,
//  Can't encode url
    ERR_URL_ENCODE
}
