package br.com.rdalloglio.scf.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenciais inválidas. Verifique usuário e senha.");
    }
}