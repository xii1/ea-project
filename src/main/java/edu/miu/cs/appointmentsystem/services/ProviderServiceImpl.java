package edu.miu.cs.appointmentsystem.services;

import edu.miu.cs.appointmentsystem.domain.*;
import edu.miu.cs.appointmentsystem.domain.Provider;
import edu.miu.cs.appointmentsystem.exceptions.ClientDataIncorrectException;
import edu.miu.cs.appointmentsystem.exceptions.SystemBaseException;
import edu.miu.cs.appointmentsystem.exceptions.provider.ProviderAlreadyExistException;
import edu.miu.cs.appointmentsystem.exceptions.provider.ProviderNotFoundException;
import edu.miu.cs.appointmentsystem.exceptions.provider.UserInProviderAlreadyExistException;
import edu.miu.cs.appointmentsystem.exceptions.provider.UserInProviderNotFoundException;
import edu.miu.cs.appointmentsystem.exceptions.user.UserNotFoundException;
import edu.miu.cs.appointmentsystem.repositories.ProviderRepository;
import edu.miu.cs.appointmentsystem.resources.ProviderMessages;
import edu.miu.cs.appointmentsystem.resources.UserMessages;
import edu.miu.cs.appointmentsystem.services.dto.ProviderDto;
import edu.miu.cs.appointmentsystem.services.dto.adapters.base.AdapterBase;
import edu.miu.cs.appointmentsystem.services.dto.base.ListDtoBase;
import edu.miu.cs.appointmentsystem.services.helpers.BeanQualifiersConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    private ProviderMessages providerMessages;
    @Autowired
    private UserMessages userMessages;
    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    @Qualifier(BeanQualifiersConstants.PROVIDER_ADAPTER)
    private AdapterBase<Provider, ProviderDto> AdapterBase;

    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Autowired
    private UserService userService;

    @Override
    public ProviderDto add(ProviderDto providerDto) throws SystemBaseException {

        Optional<Provider> optionalProvider = providerRepository.findByName(providerDto.getName());
        if (optionalProvider.isPresent()) {
            throw new ProviderAlreadyExistException(providerMessages.getAlreadyExist());
        }
        Provider provider = providerRepository.save(AdapterBase.toEntity(providerDto));
        return AdapterBase.toDto(provider);
    }

    @Override
    public ProviderDto find(Long providerId) throws SystemBaseException {
        Optional<Provider> dbProviderO = providerRepository.findById(providerId);
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));
        return AdapterBase.toDto(dbProvider);
    }

    @Override
    public ProviderDto update(Long providerId, ProviderDto provider) throws SystemBaseException {

        Optional<Provider> dbProviderO = providerRepository.findById(provider.getId());
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));

        if (dbProvider.getId().equals(provider.getId())) {

            dbProvider.setName(provider.getName());
            providerRepository.flush();
            return AdapterBase.toDto(dbProvider);
        } else {
            throw new ClientDataIncorrectException();
        }
    }

    @Override
    public void delete(Long id) throws SystemBaseException {
        Optional<Provider> dbProviderO = providerRepository.findById(id);
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));
        providerRepository.delete(dbProvider);
    }

    @Override
    public ListDtoBase<ProviderDto> findAll(Integer page, Integer size) {

        Pageable request = PageRequest.of(page, size);
        return AdapterBase.toDto(providerRepository.findAll(request));
    }

    @Override
    public ProviderDto addUsersToProvider(Long providerId, Long userId) throws SystemBaseException {
        Optional<Provider> dbProviderO = providerRepository.findById(providerId);
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));

        User user = userService.getUserById(userId);

        if (user == null)
            throw new UserNotFoundException(userMessages.getNotFound());

        if (dbProvider.getUsers().contains(user))
            throw new UserInProviderAlreadyExistException(providerMessages.getUserExist());

        dbProvider.addToUserCollection(user);
        providerRepository.flush();

        return AdapterBase.toDto(dbProvider);

    }

    @Override
    public ProviderDto removeUsersFromProvider(Long providerId, Long userId) throws SystemBaseException {
        Optional<Provider> dbProviderO = providerRepository.findById(providerId);
        Provider dbProvider = dbProviderO
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));

        User user = userService.getUserById(userId);

        if (user == null)
            throw new UserNotFoundException(userMessages.getNotFound());

        if (!dbProvider.getUsers().contains(user))
            throw new UserInProviderNotFoundException(providerMessages.getUserNotFound());

        dbProvider.removeUserFromCollection(user);
        providerRepository.flush();

        return AdapterBase.toDto(dbProvider);
    }

    @Override
    public User updateUserRole(Long providerId, Long userId, List<String> roles) throws SystemBaseException {
        final Provider dbProvider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ProviderNotFoundException(providerMessages.getNotFound()));

        final User user = userService.getUserById(userId);

        if (user == null)
            throw new UserNotFoundException(userMessages.getNotFound());

        if (!dbProvider.getUsers().contains(user))
            throw new UserInProviderNotFoundException(providerMessages.getUserNotFound());

        return userService.updateRole(user, roles.stream().map(String::toUpperCase).collect(Collectors.toList()));
    }

}
