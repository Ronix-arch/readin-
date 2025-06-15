package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.WeakCrypto;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Following;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;

import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUserDto> appUsersList(){
        return appUserRepository.findAll().stream().map(this::toDto).toList();


    }

    private AppUserDto toDto(AppUser appUser) {

        return new AppUserDto(appUser.getId(),appUser.getName(),appUser.getCreatedAt());
    }

    public AppUserDto create(CreateAppUserDto createAppUser) {
        if (createAppUser.name() == null || createAppUser.name().isEmpty() ||
                createAppUser.password() == null || createAppUser.password().isEmpty()) {
            throw ClientErrors.invalidCredentials();
        }
        if(appUserRepository.existsByName(createAppUser.name())){
            throw ClientErrors.userNameTaken(createAppUser.name());
        }
        AppUser appUser = toEntity(createAppUser);
        appUserRepository.save(appUser);
        return toDto(appUser);


}

    private AppUser toEntity(CreateAppUserDto createAppUser) {
        String hashedPassword = WeakCrypto.hashPassword(createAppUser.password());
        return new AppUser(createAppUser.name(),hashedPassword);

    }
    public AppUserDto get(String userName){       // remember username is not the key
        return appUserRepository.findByName(userName).map(this::toDto).orElseThrow(()-> ClientErrors.userNotFound(userName));

    }
    public void delete(String userName) {
        AppUser appUser = appUserRepository.findByName(userName).orElseThrow(()-> ClientErrors.userNotFound(userName));
        appUserRepository.delete(appUser);
    }


}
