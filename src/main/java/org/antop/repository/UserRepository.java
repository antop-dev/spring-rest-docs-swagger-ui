package org.antop.repository;

import org.antop.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {
    private final AtomicLong sequence = new AtomicLong();
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public User save(String name, String address) {
        User user = User.builder()
                .id(sequence.incrementAndGet())
                .name(name)
                .address(address)
                .build();
        users.put(user.getId(), user);

        return user;
    }

    public Optional<User> get(long userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
