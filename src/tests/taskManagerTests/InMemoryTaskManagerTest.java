package tests.taskManagerTests;

import managers.InMemoryTaskManager;
import tests.taskManagerTests.TaskManagerTest;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}