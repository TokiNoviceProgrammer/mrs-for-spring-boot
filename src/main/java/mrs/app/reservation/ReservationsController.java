package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.RoleName;
import mrs.domain.model.User;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.room.RoomService;

@Controller
@RequestMapping("reservations/{date}/{roomId}") // リクエストパラメーターからdata,roomIdを取得可能にする
public class ReservationsController {
	@Autowired
	RoomService roomService;
	@Autowired
	ReservationService reservationService;

	@ModelAttribute
	// @ModelAttributeによってバインドされたクラスは、自動的にModelに追加される
	// 各リクエストのModelに格納するオブジェクトを作成する
	// @ModelAttributeは@RequestMappingメソッドの引数に直接付けることもできる
	ReservationForm setUpForm() {
		ReservationForm form = new ReservationForm();
		// デフォルト値
		form.setStartTime(LocalTime.of(9, 0));
		form.setEndTime(LocalTime.of(10, 0));
		return form;
	}

	@GetMapping("")
	// @PathVariableでリクエストパスのdataをLocalDateオブジェクトにバインドし、@DateTimeFormatで日付フォーマットISO 8601 YYYY-MM-DDを指定する
	String reserveForm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model) {
		ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);
		List<Reservation> reservations = reservationService.findReservations(reservableRoomId);
		List<LocalTime> timeList = Stream.iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
				.limit(24 * 2).collect(Collectors.toList());// 00:00～30分刻みの無限ストリームを作成し、48個分で止めている
		model.addAttribute("room", roomService.findMeetingRoom(roomId));
		model.addAttribute("reservations", reservations);
		model.addAttribute("timeList", timeList);
		model.addAttribute("user", dummyUser());
		return "reservation/reserveForm";
	}

	private User dummyUser() {
		User user = new User();
		user.setUserId("test-001");
		user.setFirstName("太郎");
		user.setLastName("テスト");
		user.setRoleName(RoleName.USER);
		return user;
	}
}
