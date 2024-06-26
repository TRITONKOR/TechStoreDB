package com.tritonkor.persistence;

import com.tritonkor.persistence.entity.Entity;
import com.tritonkor.persistence.exception.RequiredFieldsException;
import com.tritonkor.persistence.exception.persistence.EntityNotFoundException;
import com.tritonkor.persistence.exception.persistence.PersistenceException;
import com.tritonkor.persistence.impl.ClientDao;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.lang.StringTemplate.STR;

public abstract class Dao<E extends Entity> {
    final String findAllSql = "SELECT * FROM %s".formatted(getTableName());
    final String findByIdSql = "%s WHERE id = ?".formatted(findAllSql);

    /**
     * Get an entity by identifier.
     *
     * @param id primary key identifier
     * @return Optional<Entity>
     */
    public Optional<E> findOneById(int id) throws RequiredFieldsException {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(findByIdSql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            E entity = null;
            if (resultSet.next()) {
                entity = buildEntity(resultSet);
            }
            return Optional.ofNullable(entity);
        } catch (SQLException e) {
            throw new EntityNotFoundException("При знаходженні запису по id.");
        }
    }

    /**
     * Get an entity by identifier with connection from other ResultSet.
     *
     * @param id primary key identifier
     * @param connection connection from ResultSet
     * @return Optional<Entity>
     */
    public Optional<E> findOneById(final int id, Connection connection) {

        try (var statement = connection.prepareStatement(findByIdSql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            E entity = null;
            if (resultSet.next()) {
                entity = buildEntity(resultSet);
            }
            return Optional.ofNullable(entity);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "При знаходженні запису в %s".formatted(ClientDao.class.getSimpleName()));
        }
    }

    /**
     * Get the entire collection of entities.
     *
     * @return collection of Entities
     */
    public List<E> findAll() {
        try (var connection = ConnectionPool.get();
                var statement = connection.prepareStatement(findAllSql)) {
            var resultSet = statement.executeQuery();
            List<E> entities = new ArrayList<>(resultSet.getFetchSize());
            while (resultSet.next()) {
                entities.add(buildEntity(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new PersistenceException("При знаходженні всіх записів");
        }
    }

    /**
     * Save the new entity to the database table.
     *
     * @param entity persistent entity
     * @return identifier of the last added record from the database
     */
    public abstract E save(E entity);

    /**
     * Update the new entity to the database table.
     *
     * @param entity updatable entity
     * @return success or failure update operation
     */
    public abstract boolean update(final E entity);

    /**
     * Delete an entity from the database table by identifier.
     *
     * @param id primary key identifier
     * @return success or failure delete operation
     */
    public boolean delete(final int id) {
        final String DELETE_SQL = "DELETE FROM %s WHERE id = ?;".formatted(getTableName());

        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException("Помилка при операції видалення рядка таблиці.");
        }
    }

    /**
     * Use only with <code>findAll(Filter filter)</code> methods.
     *
     * @param parameters for where
     * @param sql query with where and parameters
     * @return collection of Entities
     */
    protected List<E> findAllByFilter(List<Object> parameters, String sql) {
        try (var connection = ConnectionPool.get();
                var statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            var resultSet = statement.executeQuery();
            List<E> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(buildEntity(resultSet));
            }

            return entities;
        } catch (SQLException e) {
            throw new PersistenceException("При знаходженні всіх записів по фільтру");
        }
    }

    protected boolean executeByTwoParams(int firstId, int secondId, String sql) {
        try (Connection connection = ConnectionPool.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, firstId);
            statement.setInt(2, secondId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException("При прикріпленні запису.");
        }
    }

    protected List<String> tableAttributes(Class<E> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        List<String> names = new ArrayList<>();

        for (Field field : fields) {
            String fieldName = convertToSnakeCase(field.getName());

            if (field.getType().isPrimitive()) {
                names.add(fieldName.toLowerCase());
            } else {
                String fieldPackage = field.getType().getPackage() != null ? field.getType().getPackage().getName() : "";
                if (!fieldName.equals("id")) {
                    if (fieldPackage.equals(tClass.getPackage().getName())) {
                        names.add(fieldName.toLowerCase() + "_id");
                    } else {
                        names.add(fieldName.toLowerCase());
                    }
                }
            }
        }

        return names;
    }

    private static String convertToSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (String w : input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            result.append(w).append("_");
        }

        String s = result.toString();
        return s.toLowerCase().substring(0,s.length()-1);

    }

    protected String findAllSql(String tableName) {
        return STR.
                """
                SELECT * FROM \{tableName}
                """;
    }

    protected String saveSql(String tableName, E entity) {
        List<Object> values = tableValues(entity);
        List<String> listAttributes = tableAttributes();
        String attributes = String.join(", ", listAttributes);

        return STR.
                """
                INSERT INTO \{tableName}(\{attributes})
                VALUES(\{values})
                """;
    }

    protected String updateSql(String tableName, E entity) {
        List<String> listAttributes = tableAttributes();
        String attributes = listAttributes.stream().map(a -> STR."\{a} = ?").collect(Collectors.joining(", "));
        return STR.
                """
                UPDATE \{tableName}
                SET \{attributes}
                WHERE id = ?;
                """;
    }

    protected abstract E buildEntity(final ResultSet resultSet);

    protected abstract String getTableName();
    protected abstract List<String> tableAttributes();
    protected abstract List<Object> tableValues(E entity);
}
