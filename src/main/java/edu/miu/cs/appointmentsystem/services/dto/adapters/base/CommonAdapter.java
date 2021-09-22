package edu.miu.cs.appointmentsystem.services.dto.adapters.base;

import edu.miu.cs.appointmentsystem.domain.base.BaseEntity;
import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;

public abstract class CommonAdapter {

    public void updateDto(DtoBase dto, BaseEntity entity) {
        dto.setId(entity.getId());
        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(entity.getCreatedBy().getFirstName() + " " + entity.getCreatedBy().getLastName());
        }
        if (entity.getUpdatedBy() != null) {
            dto.setUpdatedBy(entity.getUpdatedBy().getFirstName() + " " + entity.getUpdatedBy().getLastName());
        }
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
    }
}
