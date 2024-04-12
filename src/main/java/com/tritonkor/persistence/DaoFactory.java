package com.tritonkor.persistence;

import com.tritonkor.persistence.impl.ClientDao;
import com.tritonkor.persistence.impl.DaoFactoryImpl;
import com.tritonkor.persistence.impl.ReviewDao;
import com.tritonkor.persistence.impl.TechniqueDao;

public abstract class DaoFactory {
    
    public static DaoFactory getDaoFactory() {
        return DaoFactoryImpl.getInstance();
    }

    /**
     * Creates a ClientDao instance.
     *
     * @return An instance of the ClientDao.
     */
    public abstract ClientDao getClientDao();

    /**
     * Creates a ReviewDao instance.
     *
     * @return An instance of the ReviewDao.
     */
    public abstract ReviewDao getReviewDao();

    /**
     * Creates a TechniqueDao instance.
     *
     * @return An instance of the TechniqueDao.
     */
    public abstract TechniqueDao getTechniqueDao();
}
