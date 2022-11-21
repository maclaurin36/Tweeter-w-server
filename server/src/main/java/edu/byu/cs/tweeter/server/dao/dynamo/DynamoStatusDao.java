package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;
import edu.byu.cs.tweeter.server.dao.StatusDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoStatusDao extends BaseDynamoDao implements StatusDao {

    private static final String PARTITION_KEY = "user_handle";
    private static final String SORT_KEY = "date_posted";
    private static final String FEED_TABLE_NAME = "feed";
    private static final String STORY_TABLE_NAME = "story";
    private static final DynamoDbTable<DynamoStatus> feedTable = enhancedClient.table(FEED_TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));
    private static final DynamoDbTable<DynamoStatus> storyTable = enhancedClient.table(STORY_TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));


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

        List<DynamoStatus> dynamoStatusList = feedTable.query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        List<Status> feed = new ArrayList<>();
        for (DynamoStatus dynamoStatusStatus : dynamoStatusList) {
            User user = new User(dynamoStatusStatus.getFirstName(), dynamoStatusStatus.getLastName(), dynamoStatusStatus.getAlias(), dynamoStatusStatus.getImageUrl());
            Status status = new Status(dynamoStatusStatus.getPost(), user, DateUtility.getStringFromDate(dynamoStatusStatus.getDate_posted()), dynamoStatusStatus.getUrls(), dynamoStatusStatus.getMentions());
            feed.add(status);
        }
        return feed;
    }

    @Override
    public List<Status> getStory(PagedRequest<Status> request) {
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

        List<DynamoStatus> dynamoStatusList = storyTable.query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        List<Status> story = new ArrayList<>();
        for (DynamoStatus dynamoStatusStatus : dynamoStatusList) {
            User user = new User(dynamoStatusStatus.getFirstName(), dynamoStatusStatus.getLastName(), dynamoStatusStatus.getAlias(), dynamoStatusStatus.getImageUrl());
            Status status = new Status(dynamoStatusStatus.getPost(), user, DateUtility.getStringFromDate(dynamoStatusStatus.getDate_posted()), dynamoStatusStatus.getUrls(), dynamoStatusStatus.getMentions());
            story.add(status);
        }
        return story;
    }

    @Override
    public void insertStatusToStory(Status status) {
        DynamoStatus dynamoStatus = new DynamoStatus(status.getUser().getAlias(), status);
        storyTable.putItem(dynamoStatus);
    }

    @Override
    public void insertStatusToFeed(String feedAlias, Status status) {
        DynamoStatus dynamoStatus = new DynamoStatus(feedAlias, status);
        feedTable.putItem(dynamoStatus);
    }
}
