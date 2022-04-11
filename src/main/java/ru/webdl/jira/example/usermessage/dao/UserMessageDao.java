package ru.webdl.jira.example.usermessage.dao;

import java.util.Optional;

public interface UserMessageDao {

    Optional<UserMessage> getById(int id);

    UserMessage create(String userKey, String text);

    void delete(UserMessage message);
}
