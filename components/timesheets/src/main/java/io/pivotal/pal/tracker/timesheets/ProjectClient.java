package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.HashMap;

public class ProjectClient {

    private HashMap<Long,ProjectInfo> mapForProject = new HashMap<>();
    private final RestOperations restOperations;
    private final String endpoint;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {

        ProjectInfo projInfo = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        mapForProject.put(projectId , projInfo);
        return projInfo;
    }

    public ProjectInfo getProjectFromCache(long projectId)
    {
        return mapForProject.get(projectId);
    }
}
