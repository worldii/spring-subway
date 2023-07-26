package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;
import static subway.exception.ErrorCode.NOT_FOUND_STATION;

import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import subway.domain.Station;
import subway.exception.SubwayException;

@SpringBootTest
public class StationDaoTest {

    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    public void setUp() {
        dataSource = new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .addScript("schema.sql")
            .addScripts("test.sql")
            .build();
        stationDao = new StationDao(new JdbcTemplate(dataSource), dataSource);
    }


    @Test
    @DisplayName("station를 조회한다.")
    void stationCreateTest() {
        // given
        Station station = new Station(1L, "서울대입구역");
        // when
        Station selectStations = stationDao.findById(station.getId())
            .orElseThrow(() -> new SubwayException(NOT_FOUND_STATION));
        // then
        assertThat(station).isEqualTo(selectStations);
    }

    @Test
    @DisplayName("station을 생성한다")
    void createStation() {
        // given
        Station station = new Station("구디역");
        // when
        int beforeSize = stationDao.findAll().size();
        stationDao.insert(station);
        // then
        assertThat(beforeSize + 1).isEqualTo(stationDao.findAll().size());
    }


    @Test
    @DisplayName("station을 삭제한다.")
    void deleteSection() {
        // given
        Station selectStation = stationDao.findById(4L)
            .orElseThrow(() -> new SubwayException(NOT_FOUND_STATION));
        // when
        stationDao.deleteById(selectStation.getId());
        // then
        Optional<Station> deleteStation = stationDao.findById(4L);
        assertThat(deleteStation).isEmpty();
    }

}
