package mrs.config;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 監査証跡
public class CurrentAuditorModel {

	private LocalDateTime createdAt;

	private String createdId;

	private LocalDateTime updatedAt;

	private String updatedId;

}
