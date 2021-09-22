package edu.miu.cs.appointmentsystem.services.dto.adapters;

import edu.miu.cs.appointmentsystem.domain.Category;
import edu.miu.cs.appointmentsystem.services.dto.CategoryDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.CommonAdapter;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component(BeanQualifiersConstants.CATEGORY_ADAPTER)
public class CategoryAdapter extends CommonAdapter implements AdapterBase<Category, CategoryDto> {
    @Override
    public Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDefaultDuration(dto.getDefaultDuration());
        return category;
    }

    @Override
    public CategoryDto toDto(Category entity) {
        CategoryDto category = new CategoryDto();
        category.setId(entity.getId());
        category.setName(entity.getName());
        category.setDefaultDuration(entity.getDefaultDuration());
        updateDto(category, entity);
        return category;
    }

    @Override
    public ListDtoBase<CategoryDto> toDto(Page<Category> data) {
        ListDtoBase<CategoryDto> categoryDtoListDtoBase = new ListDtoBase<>();
        categoryDtoListDtoBase.setTotal(data.getTotalElements());
        categoryDtoListDtoBase.setPages(data.getTotalPages());
        categoryDtoListDtoBase
                .setData(data.getContent().stream().map(this::toDto).collect(java.util.stream.Collectors.toList()));
        return categoryDtoListDtoBase;
    }
}
