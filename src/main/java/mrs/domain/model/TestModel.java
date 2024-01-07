package mrs.domain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

// Serializableは右記を行うためのもの。ネットワーク越しに転送、ファイルに保存、データベースに保存、HTTPSessionに保存。
// データを保持するクラスは特に理由がなければ implements Serializable としてしまうことが多い
@Getter
@Setter
public class TestModel implements Serializable {
	private String no;
	private String branch;
	private String val;
	private String kbn;
}
