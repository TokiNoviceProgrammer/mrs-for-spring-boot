package mrs.domain.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Serializableは右記を行うためのもの。ネットワーク越しに転送、ファイルに保存、データベースに保存、HTTPSessionに保存。
// データを保持するクラスは特に理由がなければ implements Serializable としてしまうことが多い
@Getter
@Setter
@Entity
@Table(name = "usr") // テーブル名として、userは使用できないため別途命名する
public class User implements Serializable {
	@Id
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private String accountLockFlg;
	private String initFlg;
	@Enumerated(EnumType.STRING)
	private RoleName roleName;
}
