package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
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
import se.teknikhogskolan.springcasemanagement.model.WorkItem.Status;
import se.teknikhogskolan.springcasemanagement.service.IssueService;
import se.teknikhogskolan.springcasemanagement.service.WorkItemService;
import se.teknikhogskolan.springcasemanagement.service.exception.NotFoundException;
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

        if (workItem == null || workItem.getDescription() == null) {
            throw new IncompleteException("Can not create work item without JSON body containing description");
        }

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
     * workItem status to DONE (to allow issue assignment) {"status": "DONE"
     * "issueId": 1}
     */
    @Override
    public Response updateWorkItem(Long id, WorkItemDto workItem) {

        if (workItem == null || workItem.getStatus() == null) {
            throw new IncompleteException("Can not update work item without JSON body containing status");
        }

        workItemService.setStatus(id, workItem.getStatus());

        return Response.noContent().build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/1
    @Override
    public Response deleteWorkItem(Long id) {
        workItemService.removeById(id);
        return Response.noContent().build();
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

        } else if (requestType == RequestType.HAS_ISSUE) {
            return getAllWorkItemsWithIssue();
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
        // TODO change all paging to pieces or to pages?
        Piece<WorkItem> piece = workItemService.getAllByPiece(page, size);
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

    // http://127.0.0.1:8080/jaxson/workitems?hasIssue=True
    private Response getAllWorkItemsWithIssue() {
        Collection<WorkItem> workItems = workItemService.getAllWithIssue();
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

        WorkItem workItem = workItemService.getById(id);

        if (issue == null || issue.getDescription() == null) {
            throw new IncompleteException("Can not create issue without JSON body containing description");
        }

        if (workItem.getIssue() != null) {
            throw new IllegalArgumentException("Delete assigned issue before reassigning with new issue");
        }

        if (workItem.getStatus() != Status.DONE) {
            throw new IllegalArgumentException("Set work item status to DONE before assigning issue");
        }

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
            throw new NotFoundException("No issue assigned to workItem with id " + id);
        }

        IssueDto issueDto = new IssueDto(workItem.getIssue());

        return Response.ok(issueDto).build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    // {"id": 1,"description": "some new issue description"}
    @Override
    public Response updateAssignedIssue(Long id, IssueDto issue) {

        if (issue == null || issue.getId() == null || issue.getDescription() == null) {
            throw new IncompleteException("Can not update issue. Need JSON body containing issue id and description");
        }

        issueService.updateDescription(issue.getId(), issue.getDescription());
        return Response.noContent().build();
    }

    // http://127.0.0.1:8080/jaxson/workitems/{id}/issue
    @Override
    public Response deleteAssignedIssue(Long id) {
        workItemService.removeIssueFromWorkItem(id);
        return Response.noContent().build();
    }
}