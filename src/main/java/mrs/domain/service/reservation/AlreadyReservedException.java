package mrs.domain.service.reservation;

//RuntimeExceptionを継承するだけの単純なクラス
public class AlreadyReservedException extends RuntimeException {
	public AlreadyReservedException(String message) {
		super(message);
	}
}