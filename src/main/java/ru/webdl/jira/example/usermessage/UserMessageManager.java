package ru.webdl.jira.example.usermessage;

import com.atlassian.jira.user.ApplicationUser;
import ru.webdl.jira.example.usermessage.dao.UserMessage;

import java.util.List;
import java.util.Optional;

public interface UserMessageManager {

    Optional<UserMessage> getById(int id);

    List<UserMessage> getAll(ApplicationUser user);

    UserMessage create(ApplicationUser user, String text);

    void delete(UserMessage message);
}
