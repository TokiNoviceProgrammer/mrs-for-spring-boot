package mrs.app.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mrs.domain.model.ReservableRoom;
import mrs.domain.service.reservation.RoomService;

@Controller
@RequestMapping("rooms")
public class RoomsController {
	@Autowired
	RoomService roomService;

	@GetMapping(value = "{date}")
	// URLのパスから日付を取得する。「@DateTimeFormat」でフォーマットを指定する。
	String listRooms(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
		List<ReservableRoom> rooms = roomService.findReservableRooms(date);
		model.addAttribute("rooms", rooms);
		return "room/listRooms";
	}

	@GetMapping("")
	String listRooms(Model model) {
		LocalDate today = LocalDate.now();
		model.addAttribute("date", today); // addAttributeにdateを設定すると、「@PathVariable("date")」にて取得できる
		return listRooms(today, model);
	}
}
