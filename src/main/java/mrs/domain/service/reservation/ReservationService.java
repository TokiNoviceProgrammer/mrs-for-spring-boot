package mrs.domain.service.reservation;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.RoleName;
import mrs.domain.model.User;
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

	public void cancel(Integer reservationId, User requestUser) {
		// findByIdの結果、reservationがnull(検索結果0件)の場合はReservationの初期値を設定
		Reservation reservation = findById(reservationId);
		if (RoleName.ADMIN != requestUser.getRoleName()
				&& !Objects.equals(reservation.getUser().getUserId(), requestUser.getUserId())) {
			throw new AccessDeniedException("キャンセルできません");
		}
		reservationRepository.deleteById(reservationId);
	}

	public Reservation findById(Integer reservationId) {
		return reservationRepository.findById(reservationId).orElse(new Reservation());
	}
}
