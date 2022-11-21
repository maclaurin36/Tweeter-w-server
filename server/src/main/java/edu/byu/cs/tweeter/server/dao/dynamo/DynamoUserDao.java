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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
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
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        QueryEnhancedRequest request = requestBuilder.build();

        List<Authentication> authenticationList = authTable.query(request)
                .items()
                .stream()
                .collect(Collectors.toList());

        for (Authentication auth : authenticationList) {
            Key deleteKey = Key.builder()
                    .partitionValue(auth.getToken())
                    .sortValue(auth.getExpiration())
                    .build();
            authTable.deleteItem(deleteKey);
        }
        return true;
    }

    @Override
    public AuthToken getAuthToken(AuthToken authToken) {
        Key key = Key.builder()
                .partitionValue(authToken.getToken())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        QueryEnhancedRequest request = requestBuilder.build();

        List<Authentication> authenticationList = authTable.query(request)
                .items()
                .stream()
                .limit(1)
                .collect(Collectors.toList());

        if (authenticationList.size() == 0) {
            return null;
        }

        return new AuthToken(authenticationList.get(0).getToken(), authenticationList.get(0).getExpiration());
    }

    @Override
    public void insertAuthToken(AuthToken authToken) {
        Authentication authentication = new Authentication(authToken);
        authTable.putItem(authentication);
    }

    @Override
    public void insertUser(FullUser user) {
        PasswordUser passwordUser = new PasswordUser(user);
        userTable.putItem(passwordUser);
    }

    @Override
    public List<FullUser> batchGetUser(List<String> aliases) {
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

    @Override
    public void updateUser(FullUser user) {
        PasswordUser passwordUser = new PasswordUser(user);
        userTable.updateItem(passwordUser);
    }


}
