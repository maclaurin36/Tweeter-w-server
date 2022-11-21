package edu.byu.cs.tweeter.server.dao.dynamo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserPagedResponse;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Follows;
import edu.byu.cs.tweeter.util.FakeData;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowDao extends BaseDynamoDao implements FollowDao {

    public static final String INDEX_NAME = "follows_index";
    private static final String TABLE_NAME = "follows";
    private static final String PARTITION_KEY = "follower_handle";
    private static final String SORT_KEY = "followee_handle";
    private static final String INDEX_PARTITION_KEY = "followee_handle";
    private static final String INDEX_SORT_KEY = "follower_handle";

    private static final DynamoDbTable<Follows> followTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Follows.class));

    public DynamoFollowDao() {}

    @Override
    public Boolean insertFollower(String userToBeFollowedAlias, String userToFollowAlias) {
        Follows follows = new Follows(userToBeFollowedAlias, userToFollowAlias);
        followTable.putItem(follows);
        return true;
    }

    @Override
    public Boolean deleteFollower(String userThatWasFollowed, String userThatIsNoLongerFollowing) {
        Key key = Key.builder().partitionValue(userThatIsNoLongerFollowing).sortValue(userThatWasFollowed).build();
        followTable.deleteItem(key);
        return true;
    }

    @Override
    public List<String> getFollowers(String alias, int limit, String lastItem) {
        DynamoDbIndex<Follows> index = followTable.index(INDEX_NAME);
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(false);

        if(lastItem != null && !lastItem.equals("")) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(INDEX_PARTITION_KEY, AttributeValue.builder().s(alias).build());
            startKey.put(INDEX_SORT_KEY, AttributeValue.builder().s(lastItem).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest query = requestBuilder.build();

        List<Follows> followerList = new ArrayList<>();

        SdkIterable<Page<Follows>> indexResults = index.query(query);
        PageIterable<Follows> pages = PageIterable.create(indexResults);
        pages.stream()
                .limit(1)
                .forEach(followerPage -> followerList.addAll(followerPage.items()));

        List<String> aliases = followerList.stream().map(Follows::getFollower_handle).collect(Collectors.toList());
        return aliases;
    }

    @Override
    public List<String> getFollowing(PagedRequest<String> request) {
        Key key = Key.builder()
                .partitionValue(request.getAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(true);

        if (request.getLastItem() != null && !request.getLastItem().equals("")) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(PARTITION_KEY, AttributeValue.builder().s(request.getAlias()).build());
            startKey.put(SORT_KEY, AttributeValue.builder().s(request.getLastItem()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest query = requestBuilder.build();

        List<Follows> follows = followTable.query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        List<String> following = new ArrayList<>();
        for (Follows follow : follows) {
            following.add(follow.getFollowee_handle());
        }
        return following;
    }

    @Override
    public Boolean checkFollows(IsFollowerRequest userRequest) {
        Key key = Key.builder().partitionValue(userRequest.getFollowerAlias()).sortValue(userRequest.getFolloweeAlias()).build();
        Follows follows = followTable.getItem(key);
        return follows != null;
    }
}
