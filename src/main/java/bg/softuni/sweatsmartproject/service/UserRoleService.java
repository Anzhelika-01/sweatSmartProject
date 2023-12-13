package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.UserRoleModel;
import bg.softuni.sweatsmartproject.domain.dto.view.UserRoleViewDto;
import bg.softuni.sweatsmartproject.domain.entity.User;
import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.domain.enums.RoleEnum;
import bg.softuni.sweatsmartproject.repository.UserRoleRepo;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRoleService {
    private final UserRoleRepo userRoleRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleService(UserRoleRepo userRoleRepo, ModelMapper modelMapper) {
        this.userRoleRepo = userRoleRepo;
        this.modelMapper = modelMapper;
    }

    public boolean isDbInit() {
        return userRoleRepo.count() > 0;
    }

    @PostConstruct
    public void dbInit() {
        if (!isDbInit()) {
            final List<UserRole> roles = new ArrayList<>();

            roles.add(new UserRole().setRole(RoleEnum.USER));
            roles.add(new UserRole().setRole(RoleEnum.ADMIN));

            this.userRoleRepo.saveAllAndFlush(roles);
        }
    }

    public List<UserRoleViewDto> getAll() {
        return this.userRoleRepo.findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, UserRoleViewDto.class))
                .collect(Collectors.toList());
    }

    public UserRoleModel findRoleByName(String name) {
        return this.modelMapper.map(this.userRoleRepo.findByRole(RoleEnum.valueOf(name)),
                UserRoleModel.class);
    }

    public UserRoleModel setToUser() {
        final UserRole role = this.userRoleRepo.findByRole(RoleEnum.USER);
        return this.modelMapper.map(role, UserRoleModel.class);
    }
}