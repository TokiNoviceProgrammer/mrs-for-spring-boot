package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.stereotype.Repository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
	private final ReservationMapper reservationMapper;

	public ReservationRepositoryImpl(ReservationMapper reservationMapper) {
		this.reservationMapper = reservationMapper;
	}

	public Reservation findById(Integer reservationId) {
		return reservationMapper.findById(reservationId);
	}

	public int delete(Reservation reservation) {
		return reservationMapper.delete(reservation);
	};

	public int save(Reservation reservation) {
		return reservationMapper.save(reservation);
	};

	public List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
			ReservableRoomId reservableRoomId) {
		return reservationMapper.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId);
	};
}
