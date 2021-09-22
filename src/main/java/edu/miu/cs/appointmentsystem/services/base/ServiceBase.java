package edu.miu.cs.appointmentsystem.services.base;

import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;

public interface ServiceBase<T extends DtoBase> {
    T getById(Long id);

    T save(T dto);

    T update(T dto);

    void delete(Long id);
}
