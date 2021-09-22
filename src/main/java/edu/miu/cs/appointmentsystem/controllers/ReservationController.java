package edu.miu.cs.appointmentsystem.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.controllers.base.ControllerBase;
import edu.miu.cs.appointmentsystem.controllers.helper.RestConstants;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.services.ReservationService;
import edu.miu.cs.appointmentsystem.services.dto.ReservationDto;

@RestController
@RequestMapping(RestConstants.Category.Provider.Appointment.Reservation.PREFIX)
public class ReservationController extends ControllerBase {

	@Autowired
	private ReservationService reservationService;

	@GetMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER_CLIENT)
	public ResponseEntity<?> Get(@PathVariable(RestConstants.Category.Provider.ID_PARAM_NAME) Long providerId,
			@PathVariable(RestConstants.Category.Provider.Appointment.ID_PARAM_NAME) Long appointmentId,
			@RequestParam int page, @RequestParam int size, @RequestParam AppointmentAndReservationGetType getType)
			throws SystemBaseException {
		return ResponseEntity
				.ok(CreateResponse(reservationService.findAll(getType, appointmentId, providerId, page, size)));
	}

	@PostMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER_CLIENT)
	public ResponseEntity<?> Post(
			@PathVariable(RestConstants.Category.Provider.Appointment.ID_PARAM_NAME) Long appointmentId,
			@RequestBody @Valid ReservationDto reservation) throws SystemBaseException {
		return new ResponseEntity<>(CreateResponse(reservationService.add(appointmentId, reservation)),
				HttpStatus.CREATED);
	}

	@GetMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER_CLIENT)
	public ResponseEntity<?> Get(@RequestParam AppointmentAndReservationGetType getType,
			@PathVariable(RestConstants.Category.Provider.Appointment.ID_PARAM_NAME) Long appointmentId,
			@PathVariable Long id) throws SystemBaseException {

		return ResponseEntity.ok(CreateResponse(reservationService.find(getType, appointmentId, id)));
	}

	@PutMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> Put(
			@PathVariable(RestConstants.Category.Provider.Appointment.ID_PARAM_NAME) Long appointmentId,
			@PathVariable Long id, @RequestBody @Valid ReservationDto reservation) throws SystemBaseException {
		return ResponseEntity.ok(CreateResponse(reservationService.update(appointmentId, id, reservation)));
	}

	@DeleteMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> delete(@PathVariable Long id) throws SystemBaseException {
		reservationService.delete(id);
		return ResponseEntity.ok(CreateResponse(true));
	}

}
