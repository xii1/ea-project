package edu.miu.cs.appointmentsystem.services.dto.adapters;

import edu.miu.cs.appointmentsystem.domain.Provider;
import edu.miu.cs.appointmentsystem.services.dto.ProviderDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.CommonAdapter;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component(BeanQualifiersConstants.PROVIDER_ADAPTER)
public class ProviderAdapter extends CommonAdapter implements AdapterBase<Provider, ProviderDto> {
    @Override
    public Provider toEntity(ProviderDto providerDto) {
        Provider provider = new Provider();
        provider.setId(providerDto.getId());
        provider.setName(providerDto.getName());
        return provider;
    }

    @Override
    public ProviderDto toDto(Provider provider) {
        ProviderDto providerDto = new ProviderDto();
        updateDto(providerDto, provider);
        providerDto.setName(provider.getName());
        return providerDto;
    }

    @Override
    public ListDtoBase<ProviderDto> toDto(Page<Provider> data) {
        ListDtoBase<ProviderDto> providerDtoList = new ListDtoBase<>();
        providerDtoList.setTotal(data.getTotalElements());
        providerDtoList.setPages(data.getTotalPages());
        providerDtoList
                .setData(data.getContent().stream().map(this::toDto).collect(java.util.stream.Collectors.toList()));
        return providerDtoList;
    }
}
