package com.tritonkor.persistence.impl;

import static java.util.Objects.nonNull;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.exception.persistence.EntityNotFoundException;
import com.tritonkor.persistence.exception.persistence.NoResultException;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import com.tritonkor.persistence.filter.ClientFilter;
import com.tritonkor.persistence.util.FilterSelectHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ClientDao extends Dao<Client> {

    /**
     * We get a filtered collection of entities.
     *
     * @param filter values of entity attributes for filtering
     * @return collection of Entities
     */
    public List<Client> findAll(final ClientFilter filter) {
        List<Object> parameters = new ArrayList<>();

        var filterSelectHelper =
                new FilterSelectHelper() {
                    @Override
                    protected void initParams() {
                        if (nonNull(filter.username())) {
                            super.whereSQL.add("USERNAME LIKE ?");
                            parameters.add("%" + filter.username() + "%");
                        }
                    }
                };

        String sql = filterSelectHelper.getSql(findAllSql(getTableName()));
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        return super.findAllByFilter(parameters, sql);
    }

    @Override
    public Client save(Client client) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement =
                        connection.prepareStatement(saveSql(getTableName(), client),
                                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getUsername());
            statement.setString(2, client.getPassword());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                client.setId(generatedKeys.getInt("id"));
            }
            return client;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При збереженні запису в %s. %s".formatted(ClientDao.class.getSimpleName(),
                            e.getMessage()));
        }
    }

    @Override
    public boolean update(final Client client) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(
                        updateSql(getTableName(), client))) {
            statement.setString(1, client.getUsername());
            statement.setString(2, client.getPassword());
            statement.setInt(3, client.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При оновленні запису в %s".formatted(ClientDao.class.getSimpleName()));
        }
    }

    @Override
    protected String getTableName() {
        return "CLIENTS";
    }

    @Override
    protected Client buildEntity(final ResultSet resultSet) {
        try {
            return Client.builder()
                    .id(resultSet.getInt("id"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .build();
        } catch (SQLException e) {
            throw new NoResultException(
                    "Не вдалось отримати ResultSet в %s".formatted(
                            ClientDao.class.getSimpleName()));
        }
    }

    public Optional<Client> findOneByUsername(String username) {
        return this.findAll().stream().filter(c -> c.getUsername().equals(username))
                .findFirst();
    }

    @Override
    protected List<Object> tableValues(Client client) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(client.getUsername());
        values.add(client.getPassword());
        return values;
    }

    @Override
    protected List<String> tableAttributes() {
        return tableAttributes(Client.class);
    }

    private ClientDao() {
    }

    private static class ClientDaoHolder {

        public static final ClientDao HOLDER_INSTANCE = new ClientDao();
    }

    public static ClientDao getInstance() {
        return ClientDaoHolder.HOLDER_INSTANCE;
    }
}
