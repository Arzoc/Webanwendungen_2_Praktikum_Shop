package other;

import java.util.Calendar;
import java.util.Date;

import exceptions.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;

public final class JwtManager {
	
	private static JwtManager singleton = null;
	/* TODO dynamic generation to track users */
	/* base64 hash of super secret super secret super secret */
	/* not random, secure */
	private final String secret_key = "c3VwZXIgc2VjcmV0IHN1cGVyIHNlY3JldCBzdXBlciBzZWNyZXQ=";
	private byte[] secret_key_bytes;
	
	
	private JwtManager() {
		super();
		this.secret_key_bytes = TextCodec.BASE64.decode(this.secret_key);
	}
	
	/* singleton class */
	public static JwtManager getInstance() {
		if (JwtManager.singleton == null)
			return new JwtManager();
		return JwtManager.singleton;
	}
	
	/* on invalid type value, return `now`, which invalidates token */
	private Date getTimeInFuture(long var, int type) {
		switch (type) {
		case 0: /* seconds */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 1000));
		case 1: /* minutes */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 60000));
		case 2: /* hours */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 3600000));
		default:
			return new Date();
		}
	}

	/* create new token, ready to set as header */
	public String issueToken(String email, String path) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuer(path)
				.setIssuedAt(new Date())
				.setExpiration(this.getTimeInFuture(30, 1)) /* 30 minute auto logout */
				.signWith(SignatureAlgorithm.HS512, this.secret_key_bytes)
				.compact();
	}
	
	/* get email out of decrypted token */
	public String getEmail(String token) throws InvalidTokenException {
		String email = Jwts.parser().setSigningKey(this.secret_key_bytes).parseClaimsJws(token).getBody().getSubject();
		if (email == null)
			throw new InvalidTokenException();
		return email;
	}
	
	/* check if token is valid */
	/* TODO check for correct email in claims */
	/* TOOD set auto logout timeout new */
	public String validateToken(String authHeader) throws InvalidTokenException {
		String token = authHeader.substring("Bearer".length()).trim();
		try {
			Jwts.parser().setSigningKey(this.secret_key_bytes).parseClaimsJws(token);
			return token;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new InvalidTokenException();
		}
	}

}
