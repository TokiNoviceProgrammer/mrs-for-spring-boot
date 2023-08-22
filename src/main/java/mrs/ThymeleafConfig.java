package mrs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

// Java8TimeDialectの定義はSpring boot1.4からはAutoConfigure対象であり、
// thymeleaf-extras-java8timeのdependencyを追加すればBean定義は不要になる。
@Configuration
public class ThymeleafConfig {
	@Bean
	Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}
}