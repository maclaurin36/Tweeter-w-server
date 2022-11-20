package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoFollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoUserDao;

public abstract class BaseService {
    protected DaoFactory daoFactory;

    public BaseService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    DynamoFollowDao getFollowDao() {
        return new DynamoFollowDao();
    }
    DynamoUserDao getUserDao() { return new DynamoUserDao(); }
}
