package ru.webdl.jira.example;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import ru.webdl.jira.example.usermessage.UserMessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserTimesheetServlet extends HttpServlet {
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final TemplateRenderer renderer;
    @ComponentImport
    private final WorklogManager worklogManager;
    @ComponentImport
    private final SearchService searchService;

    private final UserMessageManager userMessageManager;

    public UserTimesheetServlet(JiraAuthenticationContext jiraAuthenticationContext, TemplateRenderer renderer,
                                WorklogManager worklogManager, SearchService searchService, UserMessageManager userMessageManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.renderer = renderer;
        this.worklogManager = worklogManager;
        this.searchService = searchService;
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
        context.put("worklogs", getWorklogs(user));

        resp.setContentType("text/html;charset=utf-8");
        renderer.render("templates/timesheet/general.vm", context, resp.getWriter());
    }


    private List<Worklog> getWorklogs(ApplicationUser user) {
        String userKey = user.getKey();
        List<Issue> issues = getIssues(user);
        List<Issue> issuesWithUserWorkLog = new ArrayList<>();
        return issues.stream()
                .map(worklogManager::getByIssue)
                .flatMap(worklogs -> worklogs.stream().filter(i -> i.getAuthorKey().equals(userKey))
                        .collect(Collectors.toList()).stream())
                .collect(Collectors.toList());
    }

    private List<Issue> getIssues(ApplicationUser user) {
        String query = "project = TESTDEV"; // Для тест. задания подходящий костыль
        SearchService.ParseResult parseResult = searchService.parseQuery(user, query);
        if (parseResult.isValid()) {
            try {
                SearchResults<Issue> results = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter());
                return results.getResults();
            } catch (SearchException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }
}
