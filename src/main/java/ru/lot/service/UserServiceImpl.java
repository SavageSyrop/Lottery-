package ru.lot.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.lot.dao.UserDao;
import ru.lot.entity.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(email);
        }
        return user.get();
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User " + id + " does not exist");
        }
        return user.get();
    }

    @Override
    public User saveNewUser(User user) throws Exception {
        userDao.save(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }
}
