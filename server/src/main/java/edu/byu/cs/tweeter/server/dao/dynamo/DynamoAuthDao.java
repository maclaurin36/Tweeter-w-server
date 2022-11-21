package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Authentication;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class DynamoAuthDao extends BaseDynamoDao implements AuthDao {
    private static final String AuthTableName = "authentication";
    private static final DynamoDbTable<Authentication> authTable = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authentication.class));

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
}
