package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.dtos.UpdateAppUserDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;

import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, StorageService storageService, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUserDto> appUsersList() {
        return appUserRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<AppUserDto> search(String query) {
        return appUserRepository.findByNameContainingIgnoreCase(query).stream().map(this::toDto).toList();
    }

    private AppUserDto toDto(AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getName(),
                appUser.getCreatedAt(),
                appUser.getProfilePictureUrl(),
                appUser.getEmail(),
                appUser.getBio(),
                appUser.getRole()
        );
    }

    public AppUserDto create(CreateAppUserDto createAppUser) {
        if (createAppUser.name() == null || createAppUser.name().isEmpty() ||
                createAppUser.password() == null || createAppUser.password().isEmpty()) {
            throw ClientErrors.invalidCredentials();
        }
        if (appUserRepository.existsByName(createAppUser.name())) {
            throw ClientErrors.userNameTaken(createAppUser.name());
        }
        AppUser appUser = toEntity(createAppUser);
        appUserRepository.save(appUser);
        return toDto(appUser);
    }

    private AppUser toEntity(CreateAppUserDto createAppUser) {
        String hashedPassword = passwordEncoder.encode(createAppUser.password());
        return new AppUser(createAppUser.name(), hashedPassword);
    }

    public AppUserDto get(String userName) {
        return appUserRepository.findByName(userName).map(this::toDto).orElseThrow(() -> ClientErrors.userNotFound(userName));
    }

    public AppUserDto getById(Long userId) {
        return appUserRepository.findById(userId).map(this::toDto).orElseThrow(() -> ClientErrors.userIdNotFound(userId));
    }

    public AppUserDto updateUser(Long userId, UpdateAppUserDto updateDto, MultipartFile profilePicture) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> ClientErrors.userIdNotFound(userId));

        if (updateDto.email() != null) {
            user.setEmail(updateDto.email());
        }
        if (updateDto.bio() != null) {
            user.setBio(updateDto.bio());
        }

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String filename = storageService.store(profilePicture);
            user.setProfilePictureUrl(filename);
        }

        return toDto(appUserRepository.save(user));
    }

    public void delete(String userName) {
        AppUser appUser = appUserRepository.findByName(userName).orElseThrow(() -> ClientErrors.userNotFound(userName));
        appUserRepository.delete(appUser);
    }
}
