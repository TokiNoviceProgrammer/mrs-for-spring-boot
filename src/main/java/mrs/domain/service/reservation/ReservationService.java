package mrs.domain.service.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomRepository;

@Service
@Transactional // 各メソッドが自動でトランザクション管理されるようにする
public class ReservationService {
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservableRoomRepository reservableRoomRepository;

	public List<Reservation> findReservations(ReservableRoomId reservableRoomId) {
		return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId);
	}

	public Reservation reserve(Reservation reservation) {
		ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();
		// 悲観的ロック
		// 同じタイミングで重複する時間帯の部屋を予約しようとした場合、先に実行したリクエストのトランザクションが完了するまで待たされる
		ReservableRoom reservable = reservableRoomRepository.findOneForUpdateByReservableRoomId(reservableRoomId);
		// 予約可能かチェック
		if (reservable == null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません");
		}
		// 重複チェック(予約しようとしているReservationに重複があれば、処理を中断する)
		boolean overlap = this.findReservations(reservableRoomId).stream().anyMatch(x -> x.overlap(reservation));
		if (overlap) {
			throw new AlreadyReservedException("入力の時間帯はすでに予約済みです");
		}
		// 予約情報を登録
		// userに存在しないuserIdを設定したインスタンスをsaveすると、結合先のuserテーブルが存在しなく「非NULL制約に違反」になる
		reservationRepository.save(reservation);
		return reservation;
	}

	@PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
	// メソッド実行前に認可処理を行う
	// reservationは引数で取得
	// principalでログイン中のユーザー情報を取得可能
	public void cancel(Reservation reservation) {
		reservationRepository.delete(reservation);
	}

	public Reservation findById(Integer reservationId) {
		return reservationRepository.findById(reservationId).orElse(new Reservation());
	}
}
