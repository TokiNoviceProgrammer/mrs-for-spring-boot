package mrs.domain.model;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReservableRoom implements Serializable {
	@EmbeddedId
	private ReservableRoomId reservableRoomId;
	@ManyToOne // MeetingRoomとの関係(多対1)
	@JoinColumn(name = "room_id", insertable = false, updatable = false) // room_idと結合されるが変更が結合したテーブルに反映しないように設定する
	@MapsId("roomId") // 複合クラスのうちの外部キーとして使用するフィールド名を指定する
	private MeetingRoom meetingRoom;

	public ReservableRoom(ReservableRoomId reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public ReservableRoom() {
	}
}
