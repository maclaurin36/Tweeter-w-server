package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Authentication;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.PasswordUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.MappedTableResource;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

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

    // TODO reduce duplication
    @Override
    public void incrementFollowerCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowerCount = user.getFollowerCount() + 1;
        user.setFollowerCount(newFollowerCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
    }

    @Override
    public void incrementFollowingCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowingCount = user.getFollowingCount() + 1;
        user.setFollowingCount(newFollowingCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
    }

    @Override
    public void decrementFollowerCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowerCount = user.getFollowerCount() - 1;
        user.setFollowerCount(newFollowerCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
    }

    @Override
    public void decrementFollowingCount(String userAlias) {
        FullUser user = getUser(userAlias);
        Integer newFollowingCount = user.getFollowingCount() - 1;
        user.setFollowingCount(newFollowingCount);
        PasswordUser passwordUser = new PasswordUser(user);
        UpdateItemEnhancedRequest<PasswordUser> request = UpdateItemEnhancedRequest.builder(PasswordUser.class).item(passwordUser).build();
        userTable.updateItem(request);
    }

    @Override
    public List<FullUser> batchGetUser(List<String> aliases) {
        List<Key> keys = new ArrayList<>();
        ReadBatch.Builder<PasswordUser> readBatchBuilder = ReadBatch.builder(PasswordUser.class);
        readBatchBuilder.mappedTableResource(userTable);
        for (String alias : aliases) {
            Key key = Key.builder().partitionValue(alias).build();
            readBatchBuilder.addGetItem(key);
        }
        BatchGetItemEnhancedRequest request = BatchGetItemEnhancedRequest.builder().addReadBatch(readBatchBuilder.build()).build();
        BatchGetResultPageIterable batchResults = enhancedClient.batchGetItem(request);
        List<PasswordUser> users = batchResults.resultsForTable(userTable).stream().collect(Collectors.toList());
        List<FullUser> fullUsers = new ArrayList<>();
        users.forEach(user -> { fullUsers.add(new FullUser(user.getPassword(), user.getFirstName(), user.getLastName(), user.getUser_handle(), user.getImageUrl(), user.getFollowerCount(), user.getFollowingCount()));});
        return fullUsers;
    }


}
