package com.tritonkor.persistence.impl;

import static java.util.Objects.nonNull;

import com.tritonkor.persistence.ConnectionPool;
import com.tritonkor.persistence.Dao;
import com.tritonkor.persistence.entity.Review;
import com.tritonkor.persistence.exception.persistence.NoResultException;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import com.tritonkor.persistence.filter.ReviewFilter;
import com.tritonkor.persistence.util.FilterSelectHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao extends Dao<Review> {

    // language=H2
    private static final String FIND_ALL_SQL =
            """
            SELECT ID,
                   OWNER_ID,
                   TECHNIQUE_ID,
                   TEXT,
                   GRADE,
                   CREATE_TIME
              FROM REVIEWS
            """;
    // language=H2
    private static final String SAVE_SQL =
            """
            INSERT INTO REVIEWS(OWNER_ID, TECHNIQUE_ID, TEXT, GRADE, CREATE_TIME)
            VALUES (?, ?, ?, ?, ?);
            """;
    // language=H2
    private static final String UPDATE_SQL =
            """
            UPDATE REVIEWS
               SET OWNER_ID = ?,
                   TECHNIQUE_ID = ?,
                   TEXT = ?,
                   GRADE = ?
             WHERE ID = ?;
            """;


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

        String sql = filterSelectHelper.getSql(FIND_ALL_SQL);
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        return super.findAllByFilter(parameters, sql);
    }

    @Override
    public Review save(Review review) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement =
                        connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, review.getOwner().getId());
            statement.setInt(2, review.getTechnique().getId());
            statement.setString(3, review.getText());
            statement.setInt(4, review.getGrade().getGrade());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getInt("ID"));
                review.setCreatedAt(generatedKeys.getTimestamp("CREATE_TIME").toLocalDateTime());
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
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
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

    private ReviewDao() {}

    private static class ReviewDaoHolder {
        public static final ReviewDao HOLDER_INSTANCE = new ReviewDao();
    }

    public static ReviewDao getInstance() {
        return ReviewDaoHolder.HOLDER_INSTANCE;
    }
}
