package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.repositiry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NeedADrinkUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public NeedADrinkUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found!"));


        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {
        List<GrantedAuthority> authorities =
                userEntity.
                        getRoles().
                        stream().
                        map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name())).
                        collect(Collectors.toList());

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }
}
