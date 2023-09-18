package mrs.domain.repository.reservation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

@Mapper
public interface ReservationMapper {
	public Reservation findById(Integer reservationId);

	public int delete(Reservation reservation);

	public int save(Reservation reservation);

	// findBy + reservaleRoom.reservaleRoomId + orderBy + startTime + asc	
	public List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
			ReservableRoomId reservableRoomId);
}