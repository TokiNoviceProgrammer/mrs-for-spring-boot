package mrs.domain.service.reservation;

// RuntimeExceptionを継承するだけの単純なクラス
public class UnavailableReservationException extends RuntimeException {
	public UnavailableReservationException(String message) {
		super(message);
	}
}