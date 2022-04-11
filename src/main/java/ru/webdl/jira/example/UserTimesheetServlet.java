package ru.webdl.jira.example;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import ru.webdl.jira.example.usermessage.UserMessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserTimesheetServlet extends HttpServlet {
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final TemplateRenderer renderer;

    private final UserMessageManager userMessageManager;

    public UserTimesheetServlet(JiraAuthenticationContext jiraAuthenticationContext, TemplateRenderer renderer,
                                UserMessageManager userMessageManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.renderer = renderer;
        this.userMessageManager = userMessageManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Пример реализации проверки доступа к сервлету
        ApplicationUser user = jiraAuthenticationContext.getLoggedInUser();
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("userMessageManager", userMessageManager.getAll(user));

        resp.setContentType("text/html;charset=utf-8");
        renderer.render("templates/timesheet/general.vm", context, resp.getWriter());
    }
}
