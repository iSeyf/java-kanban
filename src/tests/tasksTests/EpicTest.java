package tests.tasksTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import interfaces.TaskManager;
import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    TaskManager taskManager;
    Epic epic = new Epic("Epic1", "DescriptionEpic1");
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        taskManager.addEpic(epic);
    }

    @Test
    public void emptySubtaskListInEpic() {
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void statusNewSubtasksInEpic() {
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic.getId());
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void statusDoneSubtasksInEpic() {
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic.getId());
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void statusNewAndDoneSubtasksInEpic() {
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic.getId());
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void statusInProgressSubtasksInEpic() {
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic.getId());
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void getEndTimeWithAndWithoutStartTimeAndDurationTest() {
        subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epic.getId());
        taskManager.addSubtask(subtask1);
        subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epic.getId());
        taskManager.addSubtask(subtask2);

        assertNull(epic.getEndTime(), "EndTime не пуст.");

        subtask1.setStartTime(LocalDateTime.MIN);
        subtask1.setDuration(30);
        taskManager.updateSubtask(subtask1);
        subtask2.setStartTime(LocalDateTime.MIN.plus(Duration.ofMinutes(35)));
        subtask2.setDuration(30);
        taskManager.updateSubtask(subtask2);
        LocalDateTime endTimeForTest = subtask2.getEndTime();

        assertEquals(endTimeForTest, epic.getEndTime(), "Неправильный расчет.");
    }
}