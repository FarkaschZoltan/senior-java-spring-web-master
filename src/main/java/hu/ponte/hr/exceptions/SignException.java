package hu.ponte.hr.exceptions;

public class SignException extends Exception{
  public SignException(){
    super();
  }

  public SignException(String message){
    super(message);
  }

  public SignException(String message, Throwable cause){
    super(message, cause);
  }
}
