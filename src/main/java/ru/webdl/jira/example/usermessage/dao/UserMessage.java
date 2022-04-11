package ru.webdl.jira.example.usermessage.dao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.StringLength;
import net.java.ao.schema.Table;

import java.util.Date;

@Preload
@Table("USER_MESSAGES")
public interface UserMessage extends Entity {
    @NotNull
    String getUserKey();
    void setUserKey(String userKey);

    @NotNull
    @StringLength(StringLength.UNLIMITED)
    String getMessage();
    void setMessage(String message);

    @NotNull
    Date getCreated();
    void setCreated(Date created);
}
