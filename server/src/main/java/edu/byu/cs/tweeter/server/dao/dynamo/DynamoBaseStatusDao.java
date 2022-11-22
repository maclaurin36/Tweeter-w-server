package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public abstract class DynamoBaseStatusDao extends BaseDynamoDao {
    private static final String PARTITION_KEY = "user_handle";
    private static final String SORT_KEY = "date_posted";

    public List<Status> getPage(PagedRequest<Status> request) {
        Key key = Key.builder()
                .partitionValue(request.getAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        if (request.getLastItem() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(PARTITION_KEY, AttributeValue.builder().s(request.getAlias()).build());
            startKey.put(SORT_KEY, AttributeValue.builder().n(DateUtility.getDateFromString(request.getLastItem().getDate()).toString()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest query = requestBuilder.build();

        List<DynamoStatus> dynamoStatusList = getTableToQuery().query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        List<Status> page = new ArrayList<>();
        for (DynamoStatus dynamoStatusStatus : dynamoStatusList) {
            User user = new User(dynamoStatusStatus.getFirstName(), dynamoStatusStatus.getLastName(), dynamoStatusStatus.getAlias(), dynamoStatusStatus.getImageUrl());
            Status status = new Status(dynamoStatusStatus.getPost(), user, DateUtility.getStringFromDate(dynamoStatusStatus.getDate_posted()), dynamoStatusStatus.getUrls(), dynamoStatusStatus.getMentions());
            page.add(status);
        }
        return page;
    }

    protected abstract DynamoDbTable<DynamoStatus> getTableToQuery();
}
