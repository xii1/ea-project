package edu.miu.cs.appointmentsystem.controllers;

import edu.miu.cs.appointmentsystem.controllers.base.ControllerBase;
import edu.miu.cs.appointmentsystem.controllers.helper.RestConstants;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.services.ProviderService;
import edu.miu.cs.appointmentsystem.services.dto.ProviderDto;
import edu.miu.cs.appointmentsystem.services.dto.ProviderUserDto;

import edu.miu.cs.appointmentsystem.services.dto.requests.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(RestConstants.Category.Provider.PREFIX_PURE)
public class ProviderController extends ControllerBase {

	@Autowired
	private ProviderService providerService;

	@GetMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> Get(@RequestParam int page, @RequestParam int size) {
		return ResponseEntity.ok(CreateResponse(providerService.findAll(page, size)));
	}

	@PostMapping()
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> Post(@RequestBody @Valid ProviderDto provider) throws SystemBaseException {
		return new ResponseEntity<>(CreateResponse(providerService.add(provider)), HttpStatus.CREATED);
	}

	@PostMapping(RestConstants.Category.Provider.User.PREFIX)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> AddUser(@PathVariable Long pId, @RequestBody ProviderUserDto providerUser)
			throws SystemBaseException {
		return new ResponseEntity<>(CreateResponse(providerService.addUsersToProvider(pId, providerUser.getUserId())),
				HttpStatus.CREATED);
	}

	@DeleteMapping(RestConstants.Category.Provider.User.REMOVE_USER)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> RemoveUser(@PathVariable Long pId, @PathVariable Long uId) throws SystemBaseException {
		return ResponseEntity.ok(CreateResponse(providerService.removeUsersFromProvider(pId, uId)));
	}

	@GetMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> Get(@PathVariable Long id) throws SystemBaseException {

		return ResponseEntity.ok(CreateResponse(providerService.find(id)));
	}

	@PutMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> Put(@PathVariable long id, @RequestBody @Valid ProviderDto provider)
			throws SystemBaseException {
		return ResponseEntity.ok(CreateResponse(providerService.update(id, provider)));
	}

	@DeleteMapping(RestConstants.SINGLE)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> Patch(@PathVariable Long id) throws SystemBaseException {
		providerService.delete(id);
		return ResponseEntity.ok(CreateResponse(true));
	}

	@PatchMapping(RestConstants.Category.Provider.User.UPDATE_USER)
	@PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
	public ResponseEntity<?> updateUser(@PathVariable Long pId, @PathVariable Long uId,
			@RequestBody UpdateUserRequest userUpdateRequest) throws SystemBaseException {
		return new ResponseEntity<>(
				CreateResponse(providerService.updateUserRole(pId, uId, userUpdateRequest.getRoles())), HttpStatus.OK);
	}

}
