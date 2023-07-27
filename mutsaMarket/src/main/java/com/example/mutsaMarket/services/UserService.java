package com.example.mutsaMarket.services;


import com.example.mutsaMarket.entity.UserEntity;
import com.example.mutsaMarket.repositories.UserRepository;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        if(userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserEntity entity = new UserEntity();
        ((CustomUserDetails)user).setId(entity.getId());

        entity.setUserId(user.getUsername());
        entity.setUserPassword(user.getPassword());
        entity.setPhone(((CustomUserDetails) user).getPhone());
        entity.setEmail(((CustomUserDetails) user).getEmail());
        entity.setAddress(((CustomUserDetails) user).getAddress());

        userRepository.save(entity);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUserId(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalEntity = userRepository.findByUserId(username);

        if(!optionalEntity.isPresent())
            throw new UsernameNotFoundException(username);

        UserEntity entity = optionalEntity.get();

        return CustomUserDetails.fromEntity(entity);
    }
}
