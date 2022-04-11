package ru.webdl.jira.example.usermessage;

import com.atlassian.jira.user.ApplicationUser;
import ru.webdl.jira.example.usermessage.dao.UserMessage;

import java.util.Optional;

public interface UserMessageManager {

    Optional<UserMessage> getById(int id);

    UserMessage create(ApplicationUser user, String text);

    void delete(UserMessage message);
}
