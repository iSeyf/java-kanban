package tests.tasksTests;

import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task = new Task("task", "description");

    @Test
    public void getEndTimeWithAndWithoutStartTimeAndDuration() {
        assertNull(task.getEndTime(), "EndTime не пуст.");

        task.setStartTime(LocalDateTime.MIN);
        task.setDuration(Duration.ofMinutes(30));
        LocalDateTime endTimeForTest = LocalDateTime.MIN.plus(Duration.ofMinutes(30));
        assertEquals(endTimeForTest, task.getEndTime(), "Неправильный расчет.");
    }
}