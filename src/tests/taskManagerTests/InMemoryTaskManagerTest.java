package tests.taskManagerTests;

import managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}