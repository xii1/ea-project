package edu.miu.cs.appointmentsystem.controllers;

import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.miu.cs.appointmentsystem.common.enums.AppointmentAndReservationGetType;
import edu.miu.cs.appointmentsystem.controllers.base.ControllerBase;
import edu.miu.cs.appointmentsystem.controllers.helper.RestConstants;
import edu.miu.cs.appointmentsystem.services.AppointmentService;
import edu.miu.cs.appointmentsystem.services.dto.AppointmentDto;

import javax.validation.Valid;

@RestController
@RequestMapping(RestConstants.Category.Provider.Appointment.PREFIX)
public class AppointmentController extends ControllerBase {
	@Autowired
	private AppointmentService serviceAppointment;

	@GetMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> Get(@RequestParam AppointmentAndReservationGetType getType, @PathVariable Long pId,
			@PathVariable Long id) throws SystemBaseException {
		return ResponseEntity.ok(CreateResponse(serviceAppointment.getById(getType, pId, id)));
	}

	@GetMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> Get(@RequestParam AppointmentAndReservationGetType getType,
			@PathVariable(RestConstants.Category.ID_PARAM_NAME) Long categoryId,
			@PathVariable(RestConstants.Category.Provider.ID_PARAM_NAME) Long providerId, Integer page, Integer size)
			throws SystemBaseException {
		return ResponseEntity
				.ok(CreateResponse(serviceAppointment.findAll(getType, categoryId, providerId, page, size)));
	}

	@PostMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> add(@PathVariable(RestConstants.Category.ID_PARAM_NAME) Long categoryId,
			@PathVariable(RestConstants.Category.Provider.ID_PARAM_NAME) Long providerId,
			@RequestBody @Valid AppointmentDto appointmentDto) throws SystemBaseException {
		return ResponseEntity
				.ok(CreateResponse(serviceAppointment.addAppointment(categoryId, providerId, appointmentDto)));
	}

	@PutMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> update(@PathVariable(RestConstants.Category.Provider.ID_PARAM_NAME) Long providerId,
			@PathVariable Long id, @RequestBody @Valid AppointmentDto appointmentDto) throws SystemBaseException {
		return ResponseEntity.ok(CreateResponse(serviceAppointment.updateAppointment(providerId, id, appointmentDto)));
	}

	@DeleteMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER)
	public ResponseEntity<?> delete(@PathVariable Long id) throws SystemBaseException {
		serviceAppointment.deleteAppointmentById(id);
		return ResponseEntity.noContent().build();
	}

}
