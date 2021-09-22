package edu.miu.cs.appointmentsystem.services.dto.base;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDtoBase<T> {
    private long total;

    private long pages;
    private List<T> data;

    public ListDtoBase() {
        this.data = new ArrayList<>();
    }
}
