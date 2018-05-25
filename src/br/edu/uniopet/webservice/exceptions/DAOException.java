package br.edu.uniopet.webservice.exceptions;

public class DAOException extends RuntimeException{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 93258454172411890L;
	private int code;
     
    public DAOException(String message, int code) {
         super(message);
         this.code = code;
    }
     
    public int getCode() {
         return code;
    }
}
