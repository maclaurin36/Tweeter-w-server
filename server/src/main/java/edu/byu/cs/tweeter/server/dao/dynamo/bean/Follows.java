package edu.byu.cs.tweeter.server.dao.dynamo.bean;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDao;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follows {
    private String follower_handle;
    private String followee_handle;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoFollowDao.INDEX_NAME)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoFollowDao.INDEX_NAME)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public Follows() {}

    public Follows(String userToBeFollowedAlias, String userToFollowAlias) {
        this.follower_handle = userToBeFollowedAlias;
        this.followee_handle = userToFollowAlias;
    }
}
