package edu.byu.cs.tweeter.server.dao.dynamo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.management.Query;

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
        QueryEnhancedRequest query = getFollowQuery(INDEX_PARTITION_KEY, INDEX_SORT_KEY, lastItem, alias, limit);

        List<Follows> followerList = new ArrayList<>();

        SdkIterable<Page<Follows>> indexResults = index.query(query);
        PageIterable<Follows> pages = PageIterable.create(indexResults);
        pages.stream()
                .limit(1)
                .forEach(followerPage -> followerList.addAll(followerPage.items()));

        return followerList.stream().map(Follows::getFollower_handle).collect(Collectors.toList());
    }

    @Override
    public List<String> getFollowing(PagedRequest<String> request) {
        QueryEnhancedRequest query = getFollowQuery(PARTITION_KEY, SORT_KEY, request.getLastItem(), request.getAlias(), request.getLimit());

        List<Follows> follows = followTable.query(query)
                .items()
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());

        return follows.stream().map(Follows::getFollowee_handle).collect(Collectors.toList());
    }

    private QueryEnhancedRequest getFollowQuery(String partitionKey, String sortKey, String lastItem, String alias, int limit) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastItem != null && !lastItem.equals("")) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(partitionKey, AttributeValue.builder().s(alias).build());
            startKey.put(sortKey, AttributeValue.builder().s(lastItem).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        return requestBuilder.build();
    }

    @Override
    public Boolean getFollows(IsFollowerRequest userRequest) {
        Key key = Key.builder().partitionValue(userRequest.getFollowerAlias()).sortValue(userRequest.getFolloweeAlias()).build();
        Follows follows = followTable.getItem(key);
        return follows != null;
    }

    @Override
    public List<String> getAllFollowers(String alias) {
        String lastFollowerAlias = null;
        boolean hasMoreFollowers = true;
        int limit = 500;
        List<String> allFollowers = new ArrayList<>();
        while (hasMoreFollowers) {
            List<String> followers = getFollowers(alias, limit, lastFollowerAlias);
            allFollowers.addAll(followers);
            if (followers.size() < limit) {
                hasMoreFollowers = false;
            }
            if (followers.size() != 0) {
                lastFollowerAlias = followers.get(followers.size() - 1);
            }
        }
        return allFollowers;
    }
}
