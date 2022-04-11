package ru.webdl.jira.example.usermessage;

import com.atlassian.jira.user.ApplicationUser;
import org.springframework.stereotype.Service;
import ru.webdl.jira.example.usermessage.dao.UserMessage;
import ru.webdl.jira.example.usermessage.dao.UserMessageDao;

import java.util.Optional;

@Service("userMessageManager")
public class UserMessageManagerImpl implements UserMessageManager {
    private final UserMessageDao dao;

    public UserMessageManagerImpl(UserMessageDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<UserMessage> getById(int id) {
        return dao.getById(id);
    }

    @Override
    public UserMessage create(ApplicationUser user, String text) {
        if (user == null || text == null || text.isEmpty()) {
            throw new UserMessageException("Переданы не все обязательные аргументы для создания сообщения");
        }
        return dao.create(user.getKey(), text);
    }

    @Override
    public void delete(UserMessage message) {
        checkCanDeleteMessage(message);
        dao.delete(message);
    }

    private void checkCanDeleteMessage(UserMessage message) {
        // Различная бизнес логика для проверки возможность удалить, например, что удалять можно только свои сообщения, или...
        if (message.getMessage().toLowerCase().contains("java")) {
            throw new UserMessageException("Нельзя просто взять, и удалить сообщение со словом java");
        }
    }
}
