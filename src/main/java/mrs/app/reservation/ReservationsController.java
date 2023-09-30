package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.service.reservation.AlreadyReservedException;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.reservation.UnavailableReservationException;
import mrs.domain.service.room.RoomService;
import mrs.domain.service.user.ReservationUserDetails;

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
		// model.addAttribute("user", dummyUser());
		return "reservation/reserveForm";
	}

	//	private User dummyUser() {
	//		User user = new User();
	//		user.setUserId("taro-yamada");
	//		user.setFirstName("太郎");
	//		user.setLastName("テスト");
	//		user.setRoleName(RoleName.USER);
	//		return user;
	//	}

	@PostMapping("")
	String reserve(@Validated ReservationForm form, BindingResult bindingResult, // @Validatedで入力チェックを行うようにする
			@AuthenticationPrincipal ReservationUserDetails userDetails, // 認証済みUserオブジェクトを取得
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model) {
		if (bindingResult.hasErrors()) {
			// 入力チェックでエラーがあった場合、フォーム画面へ遷移する
			return reserveForm(date, roomId, model);
		}
		ReservableRoom reservableRoom = new ReservableRoom(new ReservableRoomId(roomId, date));
		Reservation reservation = new Reservation();
		reservation.setStartTime(form.getStartTime());
		reservation.setEndTime(form.getEndTime());
		reservation.setReservableRoom(reservableRoom);
		reservation.setUser(userDetails.getUser());
		try {
			reservationService.reserve(reservation);
		} catch (UnavailableReservationException | AlreadyReservedException e) {
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);// 予約処理で例外発生時に、例外メッセージを画面に表示させる
		}
		// 予約一覧画面へリダイレクトする
		// リダイレクト先のURLを構築するためのパス変数は、@RequestMappingで設定したパス中のプレースホルダの変数を使用できる
		return "redirect:/reservations/{date}/{roomId}";
	}

	@PostMapping(params = "cancel")
	String cancel(@RequestParam("reservationId") Integer reservationId,
			@RequestParam("updatedAt") LocalDateTime updatedAt,
			@PathVariable("roomId") Integer roomId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
		try {
			Reservation reservation = reservationService.findById(reservationId);
			reservationService.cancel(reservation);
		} catch (AccessDeniedException e) {
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);
		}
		return "redirect:/reservations/{date}/{roomId}";
	}
}
