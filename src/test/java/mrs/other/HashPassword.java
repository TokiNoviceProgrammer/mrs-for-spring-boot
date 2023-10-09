package mrs.other;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class HashPassword {
	@Autowired
	private PasswordEncoder encoder;

	@Test
	public void encode() {
		System.out.println(encoder.encode("demo"));
		System.out.println(encoder.encode("password"));
	}
}
