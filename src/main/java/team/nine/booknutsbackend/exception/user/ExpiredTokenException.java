package team.nine.booknutsbackend.exception.user;

import io.jsonwebtoken.JwtException;

public class ExpiredTokenException extends JwtException {
    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
