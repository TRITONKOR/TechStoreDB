package com.tritonkor.persistence.impl;

import static java.util.Objects.nonNull;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.exception.persistence.NoResultException;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import com.tritonkor.persistence.filter.TechniqueFilter;
import com.tritonkor.persistence.util.FilterSelectHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class TechniqueDao extends Dao<Technique> {

    // language=H2
    private static final String FIND_ALL_SQL =
            """
            SELECT ID,
                   PRICE,
                   COMPANY,
                   MODEL
              FROM TECHNIQUES
            """;
    // language=H2
    private static final String SAVE_SQL =
            """
            INSERT INTO TECHNIQUES(PRICE, COMPANY, MODEL)
            VALUES (?, ?, ?);
            """;
    // language=H2
    private static final String UPDATE_SQL =
            """
            UPDATE TECHNIQUES
               SET PRICE = ?,
                   COMPANY = ?,
                   MODEL = ?
             WHERE ID = ?;
            """;


    /**
     * We get a filtered collection of entities.
     *
     * @param filter values of entity attributes for filtering
     * @return collection of Entities
     */
    public List<Technique> findAll(final TechniqueFilter filter) {
        List<Object> parameters = new ArrayList<>();

        var filterSelectHelper =
                new FilterSelectHelper() {
                    @Override
                    protected void initParams() {
                        if (nonNull(filter.price())) {
                            super.whereSQL.add("PRICE LIKE ?");
                            parameters.add("%" + filter.price() + "%");
                        }
                        if (nonNull(filter.company())) {
                            super.whereSQL.add("COMPANY LIKE ?");
                            parameters.add("%" + filter.company() + "%");
                        }
                        if (nonNull(filter.model())) {
                            super.whereSQL.add("MODEL LIKE ?");
                            parameters.add("%" + filter.model() + "%");
                        }
                    }
                };

        String sql = filterSelectHelper.getSql(FIND_ALL_SQL);
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        return super.findAllByFilter(parameters, sql);
    }

    @Override
    public Technique save(Technique technique) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement =
                        connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, technique.getPrice());
            statement.setString(2, technique.getCompany());
            statement.setString(3, technique.getModel());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                technique.setId(generatedKeys.getInt("ID"));
            }
            return technique;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При збереженні запису в %s".formatted(TechniqueDao.class.getSimpleName()));
        }
    }

    @Override
    public boolean update(final Technique technique) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setDouble(1, technique.getPrice());
            statement.setString(2, technique.getCompany());
            statement.setString(3, technique.getModel());
            statement.setInt(4, technique.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При оновленні запису в %s".formatted(TechniqueDao.class.getSimpleName()));
        }
    }

    @Override
    protected String getTableName() {
        return "TECHNIQUES";
    }

    @Override
    protected Technique buildEntity(final ResultSet resultSet) {
        try {
            return Technique.builder()
                    .id(resultSet.getInt("id"))
                    .price(resultSet.getDouble("price"))
                    .company(resultSet.getString("company"))
                    .model(resultSet.getString("model"))
                    .build();
        } catch (SQLException e) {
            throw new NoResultException(
                    "Не вдалось отримати ResultSet в %s".formatted(TechniqueDao.class.getSimpleName()));
        }
    }

    private TechniqueDao() {}

    private static class TechniqueDaoHolder {
        public static final TechniqueDao HOLDER_INSTANCE = new TechniqueDao();
    }

    public static TechniqueDao getInstance() {
        return TechniqueDaoHolder.HOLDER_INSTANCE;
    }
}
