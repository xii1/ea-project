package edu.miu.cs.appointmentsystem.services.dto.adapters;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import edu.miu.cs.appointmentsystem.domain.Reservation;
import edu.miu.cs.appointmentsystem.services.dto.ReservationDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.CommonAdapter;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;

@Component(BeanQualifiersConstants.RESERVATION_ADAPTER)
public class ReservationAdapter extends CommonAdapter implements AdapterBase<Reservation, ReservationDto> {
    @Override
    public Reservation toEntity(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());
        reservation.setStatus(reservationDto.getStatus());
        reservation.setNote(reservationDto.getNote());
        return reservation;
    }

    @Override
    public ReservationDto toDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        updateDto(reservationDto, reservation);
        reservationDto.setNote(reservation.getNote());
        reservationDto.setStatus(reservation.getStatus());
        return reservationDto;
    }

    @Override
    public ListDtoBase<ReservationDto> toDto(Page<Reservation> data) {
        ListDtoBase<ReservationDto> reservationDtoList = new ListDtoBase<>();
        reservationDtoList.setTotal(data.getTotalElements());
        reservationDtoList.setPages(data.getTotalPages());
        reservationDtoList
                .setData(data.getContent().stream().map(this::toDto).collect(java.util.stream.Collectors.toList()));
        return reservationDtoList;
    }
}
