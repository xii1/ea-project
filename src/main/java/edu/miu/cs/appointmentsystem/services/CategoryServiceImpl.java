package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.domain.Category;
import edu.miu.cs.appointmentsystem.exceptions.ClientDataIncorrectException;
import edu.miu.cs.appointmentsystem.repositories.CategoryRepository;
import edu.miu.cs.appointmentsystem.resources.CategoryMessages;
import edu.miu.cs.appointmentsystem.services.dto.CategoryDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMessages categoryMessages;
    @Autowired
    @Qualifier("categoryAdapter")
    private AdapterBase<Category, CategoryDto> AdapterBase;

    @Override
    public ListDtoBase<CategoryDto> getAll(Pageable page) {
        return AdapterBase.toDto(categoryRepository.findAll(page));
    }

    @Override
    public CategoryDto get(long id) throws ClientDataIncorrectException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional
                .orElseThrow(() -> new ClientDataIncorrectException(categoryMessages.getNotFound()));
        return AdapterBase.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryDto dto) throws ClientDataIncorrectException {
        Category category = AdapterBase.toEntity(dto);
        if (categoryRepository.findCategoryByName(category.getName()).isPresent())
            throw new ClientDataIncorrectException(categoryMessages.getNameAlreadyExists());
        category = categoryRepository.save(category);
        return AdapterBase.toDto(category);
    }

    @Override
    public CategoryDto update(long id, CategoryDto dto) throws ClientDataIncorrectException {
        if (dto.getId().equals(id) == false)
            throw new ClientDataIncorrectException(categoryMessages.getIdMismatch());
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional
                .orElseThrow(() -> new ClientDataIncorrectException(categoryMessages.getNotFound()));
        Optional<Category> existingCategory = categoryRepository.findCategoryByName(dto.getName());
        if (existingCategory.isPresent() && existingCategory.get().getId() != category.getId())
            throw new ClientDataIncorrectException(categoryMessages.getNameAlreadyExists());
        category.setName(dto.getName());
        category.setDefaultDuration(dto.getDefaultDuration());
        categoryRepository.save(category);
        return AdapterBase.toDto(category);
    }

    @Override
    public void delete(long id) throws ClientDataIncorrectException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional
                .orElseThrow(() -> new ClientDataIncorrectException(categoryMessages.getNotFound()));
        categoryRepository.delete(category);
    }
}
