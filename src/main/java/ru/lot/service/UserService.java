package ru.lot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.lot.entity.User;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    void deleteById(Long id);

    User getById(Long id);

    User saveNewUser(User user) throws Exception;

    List<User> getAll();
}
