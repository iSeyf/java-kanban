package tests.tasksTests;

import org.junit.jupiter.api.Test;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Subtask subtask = new Subtask("subtask", "description", 1);

    @Test
    public void getEndTimeWithAndWithoutStartTimeAndDuration() {
        assertNull(subtask.getEndTime(), "EndTime не пуст.");

        subtask.setStartTime(LocalDateTime.MIN);
        subtask.setDuration(Duration.ofMinutes(30));
        LocalDateTime endTimeForTest = LocalDateTime.MIN.plus(Duration.ofMinutes(30));
        assertEquals(endTimeForTest, subtask.getEndTime(), "Неправильный расчет");
    }
}