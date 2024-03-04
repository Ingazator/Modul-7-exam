package org.example.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserDao {
    private final SessionFactory session;

    public User getById(Long id) {
        return session.getCurrentSession().createNamedQuery("user.find.by.id", User.class)
                .setParameter("id1", id)
                .uniqueResult();
    }

    public void save(UserDto userDto) {
        session.getCurrentSession().persist(User.builder().username(userDto.username()).build());
    }

    public void delete(Long id) {
        session.getCurrentSession().remove(getById(id));
    }

    public void update(User user) {
        session.getCurrentSession().merge(user);
    }

    public List<User> getAll() {
       return session.getCurrentSession().createNamedQuery("getAll.user.by.name", User.class)
                .getResultList();
    }


}
