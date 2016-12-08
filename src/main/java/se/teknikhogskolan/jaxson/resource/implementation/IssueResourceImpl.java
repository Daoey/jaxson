package se.teknikhogskolan.jaxson.resource.implementation;

import se.teknikhogskolan.jaxson.resource.IssueResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class IssueResourceImpl implements IssueResource{
    @Override
    public String getTest() {
        return null;
    }
}