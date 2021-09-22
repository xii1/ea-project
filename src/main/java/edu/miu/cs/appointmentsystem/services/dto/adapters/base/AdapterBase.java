package edu.miu.cs.appointmentsystem.services.dto.adapters.base;

import org.springframework.data.domain.Page;

import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;

public interface AdapterBase<E extends BaseEntity, M extends DtoBase> {
    E toEntity(M dto);

    M toDto(E entity);

    ListDtoBase<M> toDto(Page<E> data);
}
