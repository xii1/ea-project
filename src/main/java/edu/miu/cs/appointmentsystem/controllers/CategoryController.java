package edu.miu.cs.appointmentsystem.controllers;

import edu.miu.cs.appointmentsystem.controllers.base.ControllerBase;
import edu.miu.cs.appointmentsystem.controllers.helper.RestConstants;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.services.CategoryService;
import edu.miu.cs.appointmentsystem.services.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(RestConstants.Category.PREFIX)
public class CategoryController extends ControllerBase {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(RestConstants.SINGLE)
    @PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<?> Get(@PathVariable Long id) throws SystemBaseException {
        return ResponseEntity.ok(CreateResponse((categoryService.get(id))));
    }

    @PostMapping
    @PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<?> Post(@Valid @RequestBody CategoryDto dto) throws SystemBaseException {
        return ResponseEntity.ok(CreateResponse(categoryService.save(dto)));
    }

    @PutMapping(RestConstants.SINGLE)
    @PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<?> Put(@PathVariable long id, @Valid @RequestBody CategoryDto dto)
            throws SystemBaseException {
        return ResponseEntity.ok(CreateResponse(categoryService.update(id, dto)));
    }

    @GetMapping
    @PreAuthorize(RestConstants.Authorization.HAS_ANY_AUTHORITY_ADMIN_PROVIDER_CLIENT)
    public ResponseEntity<?> Get(Pageable page) {
        return ResponseEntity.ok(CreateResponse((categoryService.getAll(page))));
    }

    @DeleteMapping(RestConstants.SINGLE)
    @PreAuthorize(RestConstants.Authorization.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<?> Delete(@PathVariable Long id) throws SystemBaseException {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
