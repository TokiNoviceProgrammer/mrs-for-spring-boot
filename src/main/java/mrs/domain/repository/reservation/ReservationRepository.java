package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	// findBy + reservaleRoom.reservaleRoomId + orderBy + startTime + asc	
	List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(ReservableRoomId reservableRoomId);
}
