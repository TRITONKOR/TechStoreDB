package com.tritonkor.persistence.impl;

import com.tritonkor.persistence.DaoFactory;

public class DaoFactoryImpl extends DaoFactory {

    private final ClientDao clientDao;
    private final TechniqueDao techniqueDao;
    private final ReviewDao reviewDao;

    private DaoFactoryImpl() {
        this.clientDao = ClientDao.getInstance();
        this.techniqueDao = TechniqueDao.getInstance();
        this.reviewDao = ReviewDao.getInstance();
    }

    /**
     * Gets the instance of ClientDao.
     *
     * @return The instance of ClientDao.
     */
    @Override
    public ClientDao getClientDao() {
        return clientDao;
    }

    /**
     * Gets the instance of ReviewDao.
     *
     * @return The instance of ReviewDao.
     */
    @Override
    public ReviewDao getReviewDao() {
        return reviewDao;
    }

    /**
     * Gets the instance of TechniqueDao.
     *
     * @return The instance of TechniqueDao.
     */
    @Override
    public TechniqueDao getTechniqueDao() {
        return techniqueDao;
    }

    /**
     * Gets the instance of DaoFactoryImpl (Singleton pattern).
     *
     * @return The instance of DaoFactoryImpl.
     */
    public static DaoFactoryImpl getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Holder class for the Singleton pattern.
     */
    private static class InstanceHolder {

        /** The Singleton instance of DaoFactoryImpl. */
        public static final DaoFactoryImpl INSTANCE = new DaoFactoryImpl();
    }
}
