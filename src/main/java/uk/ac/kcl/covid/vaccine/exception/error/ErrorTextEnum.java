package uk.ac.kcl.covid.vaccine.exception.error;

public enum ErrorTextEnum {

    ERROR_TEXT_400("400", "Bad Request: Invalid syntax"),
    ERROR_TEXT_401("401", "Unauthorized : Requires authorization for further operation"),
    ERROR_TEXT_403("403", "Forbidden :Not authorized to continue the operation"),
    ERROR_TEXT_404("404", "Not Found : Server cannot find the request resource"),
    ERROR_TEXT_405("405", "Not Allowed : The resource does not exist"),
    ERROR_TEXT_406("406", "Not Acceptable: Cannot find content"),
    ERROR_TEXT_407("407", "Proxy Authentication Required"),
    ERROR_TEXT_408("408", "Request Timeout"),
    ERROR_TEXT_409("409", "Conflict identified in the request"),
    ERROR_TEXT_410("410", "The request has been deleted from the server"),
    ERROR_TEXT_411("411", "Content Length header is to large"),
    ERROR_TEXT_412("412", "Preconditions in the headers failed"),
    ERROR_TEXT_413("413", "Payload too large"),
    ERROR_TEXT_414("414", "URI too long"),
    ERROR_TEXT_415("415", "Unsupported Media Type"),


    //500 Error Codes
    ERROR_TEXT_500("500", "Internal server error occured"),
    ERROR_TEXT_501("501", "Not supported"),
    ERROR_TEXT_502("502", "Bad Gateway:Invalid Response"),

    ERROR_TEXT_503("503", "Service unavailable.Try again later"),
    ERROR_TEXT_504("504", "Gateway Timeout"),
    ERROR_TEXT_505("505", "Http Version Not Supported"),
    ERROR_TEXT_511("511", "Network Authentication Required"),
    ERROR_TEXT_GENERAL("GENERAL", "Unknown error occurred");

    private final String httpErrorCode;
    private final String errorText;

    ErrorTextEnum(String httpErrorCode, String errorText) {
        this.httpErrorCode = httpErrorCode;
        this.errorText = errorText;
    }

    public static String getErrorText(String httpErrorCode) {
        for (ErrorTextEnum errorCodeEnum : ErrorTextEnum.values()) {
            if (errorCodeEnum.httpErrorCode.equalsIgnoreCase(httpErrorCode)) {
                return errorCodeEnum.errorText;
            }
        }
        return ERROR_TEXT_GENERAL.errorText;
    }

    public String getErrorText() {
        return errorText;
    }
}