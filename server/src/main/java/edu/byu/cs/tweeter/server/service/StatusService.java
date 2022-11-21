package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.service.validator.PagedRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.PostStatusRequestValidator;

public class StatusService extends BaseService {

    public StatusService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public StatusPagedResponse getFeed(PagedRequest<Status> request) {
        PagedRequestValidator<Status> pagedRequestValidator = new PagedRequestValidator<>(request, daoFactory.getUserDao());
        pagedRequestValidator.validate();
        List<Status> feedPage = daoFactory.getFeedDao().getPage(request);
        return new StatusPagedResponse(true, !(feedPage.size() < request.getLimit()), feedPage);
    }

    public PagedResponse<Status> getStory(PagedRequest<Status> request) {
        PagedRequestValidator<Status> pagedRequestValidator = new PagedRequestValidator<>(request, daoFactory.getUserDao());
        pagedRequestValidator.validate();
        List<Status> storyPage = daoFactory.getStoryDao().getPage(request);
        return new StatusPagedResponse(true, !(storyPage.size() < request.getLimit()), storyPage);
    }

    public Response postStatus(PostStatusRequest request) {
        PostStatusRequestValidator postStatusRequestValidator = new PostStatusRequestValidator(request, daoFactory.getUserDao());
        postStatusRequestValidator.validate();
        daoFactory.getStoryDao().insertStatusToStory(request.getStatus());
        List<String> followers = daoFactory.getFollowDao().getFollowers(request.getStatus().getUser().getAlias(), 100, null);
        for (String follower : followers) {
            daoFactory.getFeedDao().insertStatusToFeed(follower, request.getStatus());
        }
        return new Response(true);
    }
}
