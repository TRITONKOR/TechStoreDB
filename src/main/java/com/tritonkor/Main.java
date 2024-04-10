package com.tritonkor;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.exception.connection.ConnectionException;
import com.tritonkor.persistence.impl.ClientDao;
import com.tritonkor.persistence.impl.ReviewDao;
import com.tritonkor.persistence.impl.TechniqueDao;
import com.tritonkor.persistence.util.DbInitialization;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            DbInitialization.apply();

            ClientDao clientDao = ClientDao.getInstance();
            ReviewDao reviewDao = ReviewDao.getInstance();
            TechniqueDao techniqueDao = TechniqueDao.getInstance();

            clientDao.save(Client.builder().id(1).username("triton").password("password").build());

            List<Client> clients = clientDao.findAll();
            List<Technique> techniques = techniqueDao.findAll();
            List<Review> reviews = reviewDao.findAll();

            System.out.println(clients);
            System.out.println(techniques);
            System.out.println(reviews);

        } finally {
            try {
                ConnectionPool.closePool();
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
