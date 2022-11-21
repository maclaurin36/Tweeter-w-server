package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.Attribute;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;
import edu.byu.cs.tweeter.server.dao.StatusDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Feed;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Follows;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoStatusDao extends BaseDynamoDao implements StatusDao {

    private static final String PARTITION_KEY = "user_handle";
    private static final String SORT_KEY = "date_posted";
    private static final String TABLE_NAME = "feed";
    private static final DynamoDbTable<Feed> feedTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Feed.class));

    @Override
    public List<Status> getFeed(PagedRequest<Status> request) {
        Key key = Key.builder()
                .partitionValue(request.getAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(true);

        if (request.getLastItem() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(PARTITION_KEY, AttributeValue.builder().s(request.getAlias()).build());
            startKey.put(SORT_KEY, AttributeValue.builder().n(DateUtility.getDateFromString(request.getLastItem().getDate()).toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest query = requestBuilder.build();

        List<Feed> feedList = feedTable.query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        List<Status> feed = new ArrayList<>();
        for (Feed feedStatus : feedList) {
            User user = new User(feedStatus.getFirstName(), feedStatus.getLastName(), feedStatus.getUser_handle(), feedStatus.getImageUrl());
            Status status = new Status(feedStatus.getPost(), user, DateUtility.getStringFromDate(feedStatus.getDate_posted()), feedStatus.getUrls(), feedStatus.getMentions());
            feed.add(status);
        }
        return feed;
    }

    @Override
    public StatusPagedResponse getStory(PagedRequest<Status> request) {
        return new StatusPagedResponse(true, true, new ArrayList<>());
//        FakeData fakeData = FakeData.getInstance();
//        Pair<List<Status>, Boolean> statusResults = fakeData.getPageOfStatus(request.getLastItem(), request.getLimit());
//        return new StatusPagedResponse(true, statusResults.getSecond(), statusResults.getFirst());
    }

    @Override
    public Boolean postStatus(PostStatusRequest request) {
        return true;
    }
}
