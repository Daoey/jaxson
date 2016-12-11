package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.exception.ConflictException;
import se.teknikhogskolan.jaxson.exception.IncompleteException;
import se.teknikhogskolan.jaxson.model.IssueDto;
import se.teknikhogskolan.jaxson.model.WorkItemDto;
import se.teknikhogskolan.jaxson.model.WorkItemsRequestBean;
import se.teknikhogskolan.jaxson.model.WorkItemsRequestBean.RequestType;
import se.teknikhogskolan.jaxson.resource.WorkItemResource;
import se.teknikhogskolan.springcasemanagement.model.Issue;
import se.teknikhogskolan.springcasemanagement.model.WorkItem;
import se.teknikhogskolan.springcasemanagement.service.IssueService;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;
import se.teknikhogskolan.springcasemanagement.service.exception.NoSearchResultException;
import se.teknikhogskolan.springcasemanagement.service.wrapper.Piece;

public final class WorkItemResourceImpl implements WorkItemResource {

    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private IssueService issueService;

    @Context
    private UriInfo uriInfo;

    /*
     * http://127.0.0.1:8080/jaxson/workitems JSON body for creating a workItem
     * {"description": "some workItem description"}
     */
    @Override
    public Response createWorkItem(WorkItemDto workItem) {
        WorkItem workItemDao = workItemService.create(workItem.getDescription());
        URI location = uriInfo.getAbsolutePathBuilder().path(workItemDao.getId().toString()).build();
        return Response.created(location).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/1
    @Override
    public Response getWorkItem(Long id) {
        WorkItem workItem = workItemService.getById(id);
        WorkItemDto workItemDto = new WorkItemDto(workItem);
        return Response.ok(workItemDto).build();
    }

    /*
     * http://127.0.0.1:8080/jaxson/workitems/1 JSON body for changing a
     * workItem status to DONE (to allow issue assignment) {"status": "DONE"}
     */
    @Override
    public Response updateWorkItem(Long id, WorkItemDto workItem) {
        if (workItem.getStatus() != null) {
            workItemService.setStatus(id, workItem.getStatus());
        }
        return Response.ok().build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/1
    @Override
    public Response deleteWorkItem(Long id) {
        workItemService.removeById(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @Override
    public Response getWorkItems(WorkItemsRequestBean workItemsRequestBean) {

        RequestType requestType = workItemsRequestBean.calculateRequestType();

        if (requestType == RequestType.CONFLICTING) {
            throw new ConflictException("Could not get work items. Conflicting query parameters");
        }

        if (requestType == RequestType.INCOMPLETE) {
            throw new IncompleteException("Could not get work items. Incomplete query parameters");
        }

        if (requestType == RequestType.COMPLETED) {
            return getWorkItemsByCompletionDate(workItemsRequestBean.getCompletedAfter(),
                    workItemsRequestBean.getCompletedBefore());

        } else if (requestType == RequestType.DESCRIPTION) {
            return getWorkItemsByDescription(workItemsRequestBean.getDescription());

        } else if (requestType == RequestType.STATUS) {
            return getWorkItemsByStatus(workItemsRequestBean.getStatus());

        } else if (requestType == RequestType.CREATED) {
            return getWorkItemsByCreationDate(workItemsRequestBean.getCreatedAfter(),
                    workItemsRequestBean.getCreatedBefore());

        } else {
            return getWorkItemsByPage(workItemsRequestBean.getPage(), workItemsRequestBean.getSize());
        }
    }

    // http://127.0.0.1:8080/jaxson/workitems
    // http://127.0.0.1:8080/jaxson/workitems?page=1&size=1
    private Response getWorkItemsByPage(int page, int size) {
        Piece<WorkItem> piece = workItemService.getAllByPage(page, size);
        List<WorkItem> workItemDaos = piece.getContent();
        return convertCollectionToResponse(workItemDaos);
    }

    // http://127.0.0.1:8080/jaxson/workitems?createdAfter=2016-12-07&createdBefore=2017-01-12
    private Response getWorkItemsByCreationDate(LocalDate startDate, LocalDate endDate) {
        Collection<WorkItem> workItems = workItemService.getByCreatedBetweenDates(startDate, endDate);
        return convertCollectionToResponse(workItems);
    }

    // http://127.0.0.1:8080/jaxson/workitems?completedAfter=2016-12-10&completedBefore=2017-02-10
    private Response getWorkItemsByCompletionDate(LocalDate startDate, LocalDate endDate) {
        Collection<WorkItem> workItems = workItemService.getCompletedWorkItems(startDate, endDate);
        return convertCollectionToResponse(workItems);
    }

    // http://127.0.0.1:8080/jaxson/workitems?status=UNSTARTED
    private Response getWorkItemsByStatus(String string) {
        Collection<WorkItem> workItems = workItemService
                .getByStatus(se.teknikhogskolan.springcasemanagement.model.WorkItem.Status.valueOf(string));
        return convertCollectionToResponse(workItems);
    }

    // http://127.0.0.1:8080/jaxson/workitems?description=workItem
    private Response getWorkItemsByDescription(String description) {
        Collection<WorkItem> workItems = workItemService.getByDescriptionContains(description);
        return convertCollectionToResponse(workItems);
    }

    private Response convertCollectionToResponse(Collection<WorkItem> workItemDaos) {
        List<WorkItemDto> workItems = new ArrayList<WorkItemDto>();
        for (WorkItem w : workItemDaos) {
            workItems.add(new WorkItemDto(w));
        }
        return Response.ok(workItems).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    // {"description": "some issue description"}
    @Override
    public Response createAndAssignIssue(Long id, IssueDto issue) {
        Issue issueDao = workItemService.createIssue(issue.getDescription());
        workItemService.addIssueToWorkItem(issueDao.getId(), id);
        URI location = uriInfo.getAbsolutePath();
        return Response.created(location).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    @Override
    public Response getAssignedIssue(Long id) {

        WorkItem workItem = workItemService.getById(id);

        if (workItem.getIssue() == null) {
            throw new NoSearchResultException("No issue assigned to workItem with id " + id);
        }
        
        IssueDto issueDto = new IssueDto(workItem.getIssue());
        
        return Response.ok(issueDto).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    // {"id": 1,"description": "some new issue description"}
    @Override
    public Response updateAssignedIssue(Long id, IssueDto issue) {
        issueService.updateDescription(issue.getId(), issue.getDescription());
        return Response.ok().build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    @Override
    public Response deleteAssignedIssue(Long id) {
        workItemService.removeIssueFromWorkItem(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/issue
    //TODO Consider if there is a better URI option for this method
    @Override
    public Response getAllWorkItemsWithIssue() {
        Collection<WorkItem> workItems = workItemService.getAllWithIssue();
        return convertCollectionToResponse(workItems);
    }

    //http://127.0.0.1:8080/jaxson/workitems/{id}/users
    //{"userNumber": 1}
    //TODO Consider if there is a better URI option for this method
    @Override
    public Response assignUserToWorkItem(Long id, WorkItemDto workItem) {
        workItemService.setUser(workItem.getUserNumber(), id);
        return Response.status(Status.NO_CONTENT).build();
    }
}
