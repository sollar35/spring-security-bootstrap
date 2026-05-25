package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.UserForm;
import habsida.spring.boot_security.demo.dto.UserProfileForm;
import habsida.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {

    List<User> findAll();

    void save(UserForm userForm);

    User findByUsername(String username);

    User findById(Long id);

    void update (UserForm userForm, String newPassword);

    void delete(Long id);

    UserForm toForm(Long id);

    void updateCurrentUser(Long userId, UserForm userForm);

    void updateMyProfile(Long userId, UserProfileForm form);

    UserProfileForm toProfileForm(Long id);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndNotCurrentUser(String username, Long id);
}
