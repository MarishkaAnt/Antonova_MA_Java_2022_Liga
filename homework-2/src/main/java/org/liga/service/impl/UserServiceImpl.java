package org.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.liga.model.Task;
import org.liga.repository.UserRepository;
import org.liga.mapper.UserMapper;
import org.liga.model.User;
import org.liga.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> create(User user) {
        return Optional.of(userRepository.save(user));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>((Collection<? extends User>) userRepository.findAll());
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> update(Integer id, User user) {
        userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setId(id);
        return Optional.of(userRepository.save(user));
    }
}
