package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.resource.WorkItemResource;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;
import se.teknikhogskolan.springcasemanagement.service.wrapper.Piece;

public final class WorkItemResourceImpl implements WorkItemResource {

    @Autowired
    private WorkItemService workItemService;

    @Context
    private UriInfo uriInfo;

    /*
     * JSON body for creating a workItem { "description": "some value" }
     */

    @Override
    public Response createWorkItem(WorkItemDto workItem) {
        WorkItem workItemDao = workItemService.create(workItem.getDescription());
        URI location = uriInfo.getAbsolutePathBuilder().path(workItemDao.getId().toString()).build();
        return Response.created(location).build();
    }

    @Override
    public Response getWorkItem(Long id) {
        WorkItem workItem = workItemService.getById(id);
        WorkItemDto workItemDto = new WorkItemDto(workItem);
        return Response.ok(workItemDto).build();
    }

    @Override
    public Response getWorkItems(int page, int pageSize) {

        Piece<WorkItem> piece = workItemService.getAllByPage(page, pageSize);
        List<WorkItem> workItemDaos = piece.getContent();
        List<WorkItemDto> workItems = new ArrayList<WorkItemDto>();
        for (WorkItem w : workItemDaos) {
            workItems.add(new WorkItemDto(w));
        }
        return Response.ok(workItems).build();
    }

    /*
     * JSON body for changing a workItem status/userNumber
     * 
     * {"status": "STARTED", "userNumber": 1 }
     * 
     */
    @Override
    public Response updateWorkItem(Long id, WorkItemDto workItem) {
        if (workItem.getUserNumber() != null) {
            workItemService.setUser(workItem.getUserNumber(), id);
        }
        if (workItem.getStatus() != null) {
            workItemService.setStatus(id, workItem.getStatus());
        }
        if (workItem.getIssueId() != null) {
            workItemService.addIssueToWorkItem(workItem.getIssueId(), id);
        }
        return Response.ok().build();
    }

    @Override
    public Response deleteWorkItem(Long id, Boolean deleteOnlyAssignedIssue) {

        if (deleteOnlyAssignedIssue.equals(Boolean.TRUE)) {
            workItemService.removeIssueFromWorkItem(id);
        } else {
            workItemService.removeById(id);
        }
        
        return Response.status(Status.NO_CONTENT).build();
    }

}
