package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    @Test
    @DisplayName("Line에 Sections를 추가하는 기능.")
    void addSections() {
        // given
        Line line = new Line("2호선", "green");
        Station upStation = new Station("서울대입구역");
        Station downStation = new Station("잠실역");
        Section section = new Section(upStation, downStation, line, 10);
        Sections sections = new Sections(List.of(section));

        // when
        Line addedLine = line.addSections(sections);

        // then
        assertThat(addedLine.getSections().getSections()).contains(section);
    }

    @Test
    @DisplayName("Line에 Section을 추가하는 기능.")
    void addSection() {
        // given
        Section section = new Section(
                new Station("서울대입구역"),
                new Station("신대방역"),
                new Line("2호선", "green"),
                10
        );
        Line line = new Line("2호선", "green", new Sections(List.of(section)));

        Station upStation = new Station("신대방역");
        Station downStation = new Station("잠실역");
        Section newSection = new Section(upStation, downStation, line, 10);

        // when
        Line addedLine = line.addSection(newSection);

        // then
        assertThat(addedLine.getSections().getSections()).contains(section);
    }
}
