package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;

// JpaRepositoryを継承する場合、@Repositoryは省略可能
// このインターフェースをもとにしたプロキシクラスがspring data jpaによって自動で生成され、DIコンテナに登録される
// (上記はつまり、このインターフェースの実装クラスが自動生成されるということ)
public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId> {
	// reservable_roomから日付を指定し、room_idの昇順でデータを取得する
	List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate reservedDate);
}
