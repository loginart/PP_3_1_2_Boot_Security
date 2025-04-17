package ru.kata.spring.boot_security.demo.servise;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }



    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByName(username);
    }


    public void addUser(User user, Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByRole(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public void updateUser(Long id, User updatedUser) {
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User  not found with ID: " + id));

        user.setName(updatedUser .getName());
        user.setAge(updatedUser .getAge());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLastName(updatedUser.getLastName());


         userRepository.save(user);
    }


    public User getUser(Long id) {
        Optional<User> optionalUser  = userRepository.findById(id);

        return optionalUser.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
//
