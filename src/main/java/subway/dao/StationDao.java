package subway.dao;

import static subway.exception.ErrorCode.DUPLICATED_STATION_NAME;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.exception.SubwayException;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
        new Station(
            rs.getLong("id"),
            rs.getString("name")
        );


    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Station insert(final Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);

        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new Station(id, station.getName());

        } catch (DuplicateKeyException e) {
            throw new SubwayException(DUPLICATED_STATION_NAME);
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Station> findById(final Long id) {
        String sql = "select * from STATION where id = ?";

        try {
            Station station = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final Station newStation) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public void deleteById(final Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
