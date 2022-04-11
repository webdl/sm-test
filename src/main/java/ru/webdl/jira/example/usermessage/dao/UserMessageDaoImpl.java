package ru.webdl.jira.example.usermessage.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class UserMessageDaoImpl implements UserMessageDao {
    @ComponentImport
    private final ActiveObjects ao;

    public UserMessageDaoImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public Optional<UserMessage> getById(int id) {
        return Optional.ofNullable(ao.get(UserMessage.class, id));
    }

    @Override
    public UserMessage[] getAll(String userKey) {
        Query query = Query.select().where("USER_KEY = ?", userKey);
        return ao.find(UserMessage.class, query);
    }

    @Override
    public UserMessage create(String userKey, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("USER_KEY", userKey);
        data.put("MESSAGE", message);
        data.put("CREATED", new Date());
        return ao.create(UserMessage.class, data);
    }

    @Override
    public void delete(UserMessage message) {
        ao.delete(message);
    }

}
