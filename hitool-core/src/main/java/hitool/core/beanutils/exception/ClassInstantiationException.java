/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.exception;

@SuppressWarnings("serial")
public class ClassInstantiationException extends RuntimeException {

	 /*
    * Creates a new InstantiationException.
    */
   public ClassInstantiationException() {
       super();
   }

   /*
    * Constructs a new InstantiationException.
    *
    * @param message the reason for the exception
    */
   public ClassInstantiationException(String message) {
       super(message);
   }

   /*
    * Constructs a new InstantiationException.
    *
    * @param cause the underlying Throwable that caused this exception to be thrown.
    */
   public ClassInstantiationException(Throwable cause) {
       super(cause);
   }

   /*
    * Constructs a new InstantiationException.
    *
    * @param message the reason for the exception
    * @param cause   the underlying Throwable that caused this exception to be thrown.
    */
   public ClassInstantiationException(String message, Throwable cause) {
       super(message, cause);
   }
}
