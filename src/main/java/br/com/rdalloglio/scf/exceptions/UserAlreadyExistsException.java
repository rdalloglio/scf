package br.com.rdalloglio.scf.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Usuário já cadastrado: " + username);
    }
}