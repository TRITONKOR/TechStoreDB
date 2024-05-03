package com.tritonkor.persistence.impl;

import static java.util.Objects.nonNull;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Client;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.entity.Technique;
import com.tritonkor.persistence.exception.persistence.NoResultException;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import com.tritonkor.persistence.filter.ReviewFilter;
import com.tritonkor.persistence.util.FilterSelectHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao extends Dao<Review> {
    /**
     * We get a filtered collection of entities.
     *
     * @param filter values of entity attributes for filtering
     * @return collection of Entities
     */
    public List<Review> findAll(final ReviewFilter filter) {
        List<Object> parameters = new ArrayList<>();

        var filterSelectHelper =
                new FilterSelectHelper() {
                    @Override
                    protected void initParams() {
                        if (nonNull(filter.ownerID())) {
                            super.whereSQL.add("OWNER_ID LIKE ?");
                            parameters.add("%" + filter.ownerID() + "%");
                        }
                        if (nonNull(filter.techniqueID())) {
                            super.whereSQL.add("TECHNIQUE_ID LIKE ?");
                            parameters.add("%" + filter.techniqueID() + "%");
                        }
                        if (nonNull(filter.text())) {
                            super.whereSQL.add("TEXT LIKE ?");
                            parameters.add("%" + filter.text() + "%");
                        }
                        if (nonNull(filter.grade())) {
                            super.whereSQL.add("GRADE LIKE ?");
                            parameters.add("%" + filter.grade() + "%");
                        }
                        if (nonNull(filter.createdAt())) {
                            super.whereSQL.add("CREATE_TIME LIKE ?");
                            parameters.add("%" + filter.createdAt() + "%");
                        }
                    }
                };

        String sql = filterSelectHelper.getSql(findAllSql(getTableName()));
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        return super.findAllByFilter(parameters, sql);
    }

    @Override
    public Review save(Review review) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement =
                        connection.prepareStatement(saveSql(getTableName(), review), Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, review.getOwner().getId());
            statement.setInt(2, review.getTechnique().getId());
            statement.setString(3, review.getText());
            statement.setInt(4, review.getGrade().getGrade());
            statement.setTimestamp(5, Timestamp.valueOf(review.getCreatedAt()));

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getInt("id"));
                review.setCreatedAt(generatedKeys.getTimestamp("create_time").toLocalDateTime());
            }
            return review;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При збереженні запису в %s".formatted(ReviewDao.class.getSimpleName()));
        }
    }

    @Override
    public boolean update(final Review review) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(updateSql(getTableName(), review))) {
            statement.setInt(1, review.getOwner().getId());
            statement.setInt(2, review.getTechnique().getId());
            statement.setString(3, review.getText());
            statement.setInt(4, review.getGrade().getGrade());
            statement.setInt(5, review.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При оновленні запису в %s".formatted(ReviewDao.class.getSimpleName()));
        }
    }

    @Override
    protected String getTableName() {
        return "REVIEWS";
    }

    @Override
    protected Review buildEntity(final ResultSet resultSet) {
        ClientDao clientDao = ClientDao.getInstance();
        TechniqueDao techniqueDao = TechniqueDao.getInstance();
        try {
            int ownerId = resultSet.getInt("OWNER_ID");
            int techniqueId = resultSet.getInt("TECHNIQUE_ID");
            Connection connection = resultSet.getStatement().getConnection();
            return Review.builder()
                    .id(resultSet.getInt("ID"))
                    .owner(clientDao.findOneById(ownerId, connection).orElseThrow())
                    .technique(techniqueDao.findOneById(techniqueId, connection).orElseThrow())
                    .text(resultSet.getString("TEXT"))
                    .grade(resultSet.getInt("GRADE"))
                    .createdAt(resultSet.getTimestamp("CREATE_TIME").toLocalDateTime())
                    .build();
        } catch (SQLException e) {
            throw new NoResultException(
                    "Не вдалось отримати ResultSet в %s".formatted(ReviewDao.class.getSimpleName()));
        }
    }

    public List<Review> findByOwner(Client client) {
        return this.findAll().stream().filter(r -> r.getOwner().equals(client)).toList();
    }

    public List<Review> findByTechnique(Technique technique) {
        return this.findAll().stream().filter(r -> r.getTechnique().equals(technique)).toList();
    }

    @Override
    protected List<Object> tableValues(Review review) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(review.getOwner().getId());
        values.add(review.getTechnique().getId());
        values.add(review.getText());
        values.add(review.getGrade());
        values.add(review.getCreatedAt());
        return values;
    }

    @Override
    protected List<String> tableAttributes() {
        return tableAttributes(Review.class);
    }

    private ReviewDao() {}

    private static class ReviewDaoHolder {
        public static final ReviewDao HOLDER_INSTANCE = new ReviewDao();
    }

    public static ReviewDao getInstance() {
        return ReviewDaoHolder.HOLDER_INSTANCE;
    }
}
