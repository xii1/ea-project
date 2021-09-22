package edu.miu.cs.appointmentsystem.services;


import edu.miu.cs.appointmentsystem.exceptions.ClientDataIncorrectException;
import edu.miu.cs.appointmentsystem.services.dto.CategoryDto;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import org.springframework.data.domain.Pageable;


public interface CategoryService {
    ListDtoBase<CategoryDto> getAll(Pageable page);

    CategoryDto get(long id) throws ClientDataIncorrectException;

    CategoryDto save(CategoryDto dto) throws ClientDataIncorrectException;

    CategoryDto update(long id, CategoryDto dto) throws ClientDataIncorrectException;

    void delete(long id) throws ClientDataIncorrectException;
}
