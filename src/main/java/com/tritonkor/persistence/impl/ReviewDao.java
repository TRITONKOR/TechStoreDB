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
            INSERT INTO REVIEWS(owner_id, technique_id, text, grade, create_time)
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

    // language=H2
    private static final String GET_TECHNIQUE_SQL =
            """
            SELECT t.id,
                   t.price,
                   t.company,
                   t.model
              FROM techniques AS t
                   JOIN techniques_reviews AS t_r
                     ON t.id = t_r.technique_id
             WHERE t_r.review_id = ?;
            """;

    // language=H2
    private static final String ATTACH_TO_TECHNIQUE_SQL =
            """
            INSERT INTO techniques_reviews(technique_id, review_id)
            VALUES (?, ?);
            """;
    // language=H2
    private static final String DETACH_FROM_TECHNIQUE_SQL =
            """
            DELETE FROM techniques_reviews
                  WHERE technique_id = ? AND review_id = ?;
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

    public boolean attachToReview(int techniqueId, int reviewId) {
        return super.executeByTwoParams(techniqueId, reviewId, ATTACH_TO_TECHNIQUE_SQL);
    }

    public boolean detachFromReview(int techniqueId, int reviewId) {
        return super.executeByTwoParams(techniqueId, reviewId, DETACH_FROM_TECHNIQUE_SQL);
    }

    public List<Technique> findAllTechniques(int reviewId) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(GET_TECHNIQUE_SQL)) {
            statement.setInt(1, reviewId);
            ResultSet resultSet = statement.executeQuery();
            var techniques = new ArrayList<Technique>(resultSet.getFetchSize());
            while (resultSet.next()) {
                techniques.add(TechniqueDao.getInstance().buildEntity(resultSet));
            }
            return techniques;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При знаходженні всіх записів в %s".formatted(TechniqueDao.class.getSimpleName()));
        }
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

    private ReviewDao() {}

    private static class ReviewDaoHolder {
        public static final ReviewDao HOLDER_INSTANCE = new ReviewDao();
    }

    public static ReviewDao getInstance() {
        return ReviewDaoHolder.HOLDER_INSTANCE;
    }
}
