package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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

    /*
     * JSON body for changing a workItem status to DONE (to allow issue
     * assignment), assigning userNumber 1 and adding a issue to it. (Assigning
     * the issue to it will change status to STARTED)
     *
     * {"status": "DONE", "userNumber": 1, "issueId": 1}
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

        if (deleteOnlyAssignedIssue != null && deleteOnlyAssignedIssue.equals(Boolean.TRUE)) {
            workItemService.removeIssueFromWorkItem(id);
        } else {
            //Removes assigned issues too
            workItemService.removeById(id);
        }

        return Response.status(Status.NO_CONTENT).build();
    }

    @Override
    public Response deleteAssignedIssue(Long id) {
        // TODO Remove this method or remove ability to delete only assigned
        // issue in deleteWorkItem
        workItemService.removeIssueFromWorkItem(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @Override
    public Response getWorkItems(int page, int size, String description, Long userNumber, String status,
            String createdAfter, String createdBefore, Long teamId, Boolean hasIssue, String completedAfter,
            String completedBefore) {

        if (description != null) {
            return getWorkItemsByDescription();
        } else if (userNumber != null) {
            return getWorkItemsByUserNumber();
        } else if (status != null) {
            return getWorkItemsByStatus();
        } else if (createdAfter != null && createdBefore != null) {
            return getWorkItemsByCreationDate();
        } else if (teamId != null) {
            return getWorkItemsByTeamId();
        } else if (hasIssue != null && hasIssue.equals(Boolean.TRUE)) {
            return getWorkItemsWithIssues();
        } else if (completedAfter != null && completedBefore != null) {
            return getWorkItemsByCompletionDate();
        } else {
            return getWorkItemsByPage(page, size);
        }

    }

    private Response getWorkItemsByPage(int page, int size) {
        Piece<WorkItem> piece = workItemService.getAllByPage(page, size);
        List<WorkItem> workItemDaos = piece.getContent();
        return convertCollectionToResponse(workItemDaos);
    }

    private Response getWorkItemsByCreationDate() {
        // TODO Auto-generated method stub
        return null;
    }

    private Response getWorkItemsWithIssues() {
        Collection<WorkItem> workItemDaos = workItemService.getAllWithIssue();
        return convertCollectionToResponse(workItemDaos);
    }

    private Response getWorkItemsByTeamId() {
        // TODO Auto-generated method stub
        return null;
    }

    private Response getWorkItemsByCompletionDate() {
        // TODO Auto-generated method stub
        return null;
    }

    private Response getWorkItemsByStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    private Response getWorkItemsByUserNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    private Response getWorkItemsByDescription() {
        // TODO Auto-generated method stub
        return null;
    }


    private Response convertCollectionToResponse(Collection<WorkItem> workItemDaos) {
        List<WorkItemDto> workItems = new ArrayList<WorkItemDto>();
        for (WorkItem w : workItemDaos) {
            workItems.add(new WorkItemDto(w));
        }
        return Response.ok(workItems).build();
    }
}
