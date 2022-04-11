package ru.webdl.jira.example.usermessage.dao;

import java.util.Optional;

public interface UserMessageDao {

    Optional<UserMessage> getById(int id);

    UserMessage[] getAll(String userKey);

    UserMessage create(String userKey, String text);

    void delete(UserMessage message);
}
