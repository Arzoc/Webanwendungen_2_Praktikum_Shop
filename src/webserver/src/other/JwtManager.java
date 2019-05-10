package other;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;

import exceptions.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public final class JwtManager {
	
	private static JwtManager singleton = null;
	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;
	
	private JwtManager() {
		super();
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA"); // EC (Elliptic curves) -> initialize / RSA
			gen.initialize(2048);
			KeyPair pair = gen.generateKeyPair();
			this.privateKey = pair.getPrivate();
			this.publicKey = pair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Failed to generate key");
		}
	}
	
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

	public String issueToken(String email, String path) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuer(path)
				.setIssuedAt(new Date())
				.setExpiration(this.getTimeInFuture(30, 1))
				.signWith(SignatureAlgorithm.HS512, this.privateKey)
				.compact();
	}
	
	public String getEmail(String token) throws InvalidTokenException {
		String email = Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(token).getBody().getSubject();
		if (email == null)
			throw new InvalidTokenException();
		return email;
	}
	
	public String validateToken(String authHeader) throws InvalidTokenException {
		String token = authHeader.substring("Bearer".length()).trim();
		try {
			Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(token);
			return token;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new InvalidTokenException();
		}
	}

}
