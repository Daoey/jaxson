package se.teknikhogskolan.jaxson.resource.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import se.teknikhogskolan.jaxson.model.WorkItemModel;
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
     * Content type: json Body:
     * 
     * { "description": "some value" }
     */

    @Override
    public Response createWorkItem(WorkItemModel workItem) {
        WorkItem workItemDao = workItemService.create(workItem.getDescription());
        URI location = uriInfo.getAbsolutePathBuilder().path(workItemDao.getId().toString()).build();
        return Response.created(location).build();
    }

    @Override
    public Response getWorkItem(Long id) {
        WorkItem workItem = workItemService.getById(id);
        WorkItemModel workItemModel = new WorkItemModel(workItem);
        return Response.ok(workItemModel).build();
    }

    @Override
    public Response getWorkItems(int page, int pageSize) {

        Piece<WorkItem> piece = workItemService.getAllByPage(page, pageSize);
        List<WorkItem> workItemDaos = piece.getContent();
        List<WorkItemModel> workItems = new ArrayList<WorkItemModel>();
        for (WorkItem w : workItemDaos) {
            workItems.add(new WorkItemModel(w));
        }
        return Response.ok(workItems).build();
    }

    @Override
    public Response updateWorkItem(Long id, WorkItemModel workItem) {
        if (workItem.getUserNumber() != null) {
            workItemService.setUser(workItem.getUserNumber(), workItem.getId());
        }
        if (workItem.getStatus() != null) {
            workItemService.setStatus(workItem.getId(), workItem.getStatus());
        }
        return Response.ok().build();
    }

    @Override
    public Response deleteWorkItem(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
