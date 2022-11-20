package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Authentication;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.PasswordUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedResponse;

public class DynamoUserDao extends BaseDynamoDao implements UserDao {

    private static final String AuthTableName = "authentication";
    private static final String UserTableName = "user";
    private static final DynamoDbTable<Authentication> authTable = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authentication.class));
    private static final DynamoDbTable<PasswordUser> userTable = enhancedClient.table(UserTableName, TableSchema.fromBean(PasswordUser.class));

    @Override
    public FullUser getUser(String alias) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        PasswordUser passwordUser = userTable.getItem(key);

        if (passwordUser == null) {
            return null;
        }

        return new FullUser(passwordUser.getPassword(), passwordUser.getFirstName(), passwordUser.getLastName(), passwordUser.getUser_handle(), passwordUser.getImageUrl(), passwordUser.getFollowerCount(), passwordUser.getFollowingCount());
    }

    @Override
    public Boolean deleteAuthToken(AuthToken authToken) {
        Key key = Key.builder()
                .partitionValue(authToken.getToken())
                .sortValue(authToken.getExpiration())
                .build();
        authTable.deleteItem(key);
        return true;
    }

    @Override
    public void insertAuthToken(String alias, AuthToken authToken) {
        Authentication authentication = new Authentication(alias, authToken);
        authTable.putItem(authentication);
    }

    @Override
    public void insertUser(FullUser user) {
        PasswordUser passwordUser = new PasswordUser(user);
        userTable.putItem(passwordUser);
    }

    @Override
    public Integer incrementFollowerCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowerCount = user.getFollowerCount() + 1;
        user.setFollowerCount(newFollowerCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
        return newFollowerCount;
    }

    @Override
    public Integer incrementFollowingCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowingCount = user.getFollowingCount() + 1;
        user.setFollowingCount(newFollowingCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
        return newFollowingCount;
    }


}
