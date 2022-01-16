package no.bjornhjelle.authentication.exceptions;

import lombok.Getter;

/**
 * An enumeration of error codes and associated i18n message keys for order
 * related validation errors.
 *
 * @author : github.com/sharmasourabh
 * @project : Chapter03 - Modern API Development with Spring and Spring Boot
 * @created : 11/04/2020, Tuesday
 **/
@Getter
public enum ErrorCode {
  // Internal Errors: 1 to 0999
  GENERIC_ERROR("PACKT-0001", "The system is unable to complete the request. Contact system support."),
  HTTP_MEDIATYPE_NOT_SUPPORTED("PACKT-0002", "Requested media type is not supported. Please use application/json or application/xml as 'Content-Type' header value"),
  HTTP_MESSAGE_NOT_WRITABLE("PACKT-0003", "Missing 'Accept' header. Please add 'Accept' header."),
  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("PACKT-0004", "Requested 'Accept' header value is not supported. Please use application/json or application/xml as 'Accept' value"),
  JSON_PARSE_ERROR("PACKT-0005", "Make sure request payload should be a valid JSON object."),
  RESOURCE_NOT_FOUND("PACKT-0010", "Requested resource not found."),
  UNAUTHORIZED("PACKT-0015", "Unauthorized"),
  CUSTOMER_NOT_FOUND("PACKT-0011", "Requested customer not found."),
  HTTP_MESSAGE_NOT_READABLE("PACKT-0006", "Make sure request payload should be a valid JSON or XML object according to 'Content-Type'.");

  private String errCode;
  private String errMsgKey;

  ErrorCode(final String errCode, final String errMsgKey) {
    this.errCode = errCode;
    this.errMsgKey = errMsgKey;
  }

}
