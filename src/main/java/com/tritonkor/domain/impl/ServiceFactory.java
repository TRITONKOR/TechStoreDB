package com.tritonkor.domain.impl;

import com.tritonkor.domain.contract.ClientService;
import com.tritonkor.domain.contract.ReviewService;
import com.tritonkor.domain.contract.TechniqueService;
import com.tritonkor.domain.exception.DependencyException;
import com.tritonkor.domain.handler.HandlerFactory;
import com.tritonkor.persistence.DaoFactory;

public class ServiceFactory {
    private static ServiceFactory INSTANCE;

    private final ClientService clientService;
    private final ReviewService reviewService;
    private final TechniqueService techniqueService;

    private final DaoFactory daoFactory;

    public ServiceFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        HandlerFactory handlerFactory = HandlerFactory.getInstance();

        clientService = new ClientServiceImpl(daoFactory.getClientDao());
        techniqueService = new TechniqueServiceImpl(daoFactory.getTechniqueDao());
        reviewService = new ReviewServiceImpl(daoFactory.getReviewDao());
    }

    /**
     * Returns the singleton instance of the ServiceFactory. This method should only be used when it
     * is certain that a DaoFactory object exists.
     *
     * @return the singleton instance of the ServiceFactory.
     * @throws DependencyException if a DaoFactory object is not created before using the
     *                             ServiceFactory.
     */
    public static ServiceFactory getInstance() {
        if (INSTANCE.daoFactory != null) {
            return INSTANCE;
        } else {
            throw new DependencyException(
                    "You forgot to create the DaoFactory object before using the ServiceFactory.");
        }
    }

    /**
     * Returns the singleton instance of the ServiceFactory, creating it if it doesn't exist.
     *
     * @param daoFactory the dao factory used for creating dao objects.
     * @return the singleton instance of the ServiceFactory.
     */
    public static ServiceFactory getInstance(DaoFactory daoFactory) {
        if (INSTANCE == null) {
            synchronized (ServiceFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceFactory(daoFactory);
                }
            }
        }

        return INSTANCE;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public ReviewService getReviewService() {
        return reviewService;
    }

    public TechniqueService getTechniqueService() {
        return techniqueService;
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
