package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.domain.User;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.services.dto.ProviderDto;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;

import java.util.List;

public interface ProviderService {
    ProviderDto add(ProviderDto provider) throws SystemBaseException;

    ProviderDto find(Long providerId) throws SystemBaseException;

    ProviderDto update(Long providerId, ProviderDto provider) throws SystemBaseException;

    void delete(Long id) throws SystemBaseException;

    ListDtoBase<ProviderDto> findAll(Integer page, Integer size);

    ProviderDto addUsersToProvider(Long providerId, Long userId) throws SystemBaseException;

    ProviderDto removeUsersFromProvider(Long providerId, Long userId) throws SystemBaseException;

    User updateUserRole(Long providerId, Long userId, List<String> roles) throws SystemBaseException;
}
