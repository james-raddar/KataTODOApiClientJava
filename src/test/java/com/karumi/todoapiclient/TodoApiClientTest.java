/*
 *   Copyright (C) 2016 Karumi.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.karumi.todoapiclient;

import com.karumi.todoapiclient.dto.TaskDto;
import com.karumi.todoapiclient.exception.ItemNotFoundException;
import com.karumi.todoapiclient.exception.UnknownErrorException;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TodoApiClientTest extends MockWebServerTest {

  private static final String ANY_TASK_ID = "1";
  private static final TaskDto ANY_TASK = new TaskDto("1", "2", "Finish this kata", false);

  private TodoApiClient apiClient;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    String mockWebServerEndpoint = getBaseEndpoint();
    apiClient = new TodoApiClient(mockWebServerEndpoint);
  }

  @Test
  public void sendsAcceptAndContentTypeHeaders() throws Exception {
    enqueueMockResponse();

    apiClient.getAllTasks();

    assertRequestContainsHeader("Accept", "application/json");
  }

  // Este es el primer test que se deberia hacer (PATH). Los siguientes seria el de comprobar
  // los verbos HTTP y la comprobacion del Header
  @Test public void sendsGetAllTaskRequestToTheCorrectEndpoint() throws Exception {
    enqueueMockResponse();

    apiClient.getAllTasks();

    assertGetRequestSentTo("/todos");
  }

  /*** TESTS FOR GET ALL TASKS ***/

  @Test public void parsesTasksProperlyGettingAllTheTasks() throws Exception {
    enqueueMockResponse(200, "getTasksResponse.json");

    List<TaskDto> tasks = apiClient.getAllTasks();

    assertEquals(tasks.size(), 200);
    assertTaskContainsExpectedValues(tasks.get(0));
  }

  @Test(expected = UnknownErrorException.class)
  public void throwsUnknownErrorExceptionIfThereIsNotHandledErrorGettingAllTasks()
          throws Exception {
    enqueueMockResponse(418);

    apiClient.getAllTasks();
  }

  // Test 1 (NOTA: Hay que modificar la clase DefaultHeadersInterceptor y añadirlo)
  @Test public void sendsAcceptLanguageHeaders() throws Exception {
    enqueueMockResponse();

    apiClient.getAllTasks();

    assertRequestContainsHeader("Accept-Language", "es");
  }

  // Test 2
  @Test
  public void parsesPropertlyGettingAnEmptyTaskList() throws Exception {
      enqueueMockResponse(200, "emptyListResponse.json");

      List<TaskDto> tasks = apiClient.getAllTasks();

      assertEquals(0, tasks.size());
  }

  // Test 3
  @Test(expected = ItemNotFoundException.class)
  public void parsesPropertlyGettingAn404FromTasksList() throws Exception {
      enqueueMockResponse(404);

      apiClient.getAllTasks();
  }

  // Test 4
  @Test
  public void parsesPropertlyGettingAnEmptyResponse() throws Exception {
      enqueueMockResponse(200, "emptyResponse.json");

      List<TaskDto> tasks = apiClient.getAllTasks();

      assertNull( tasks);
  }

  /*** TESTS FOR GET TASK BY ID ***/

  // Test 5
  @Test
  public void sendsGetTaskByIdRequestToTheCorrectEndpoint() throws Exception {
      enqueueMockResponse();

      apiClient.getTaskById(ANY_TASK_ID);

      assertGetRequestSentTo("/todos/" + ANY_TASK_ID);
  }

  // Test 6
  @Test
  public void parsesTasksProperlyGettingTaskById() throws Exception {
      enqueueMockResponse(200, "getTaskByIdResponse.json");

      TaskDto task = apiClient.getTaskById(ANY_TASK_ID);

      assertTaskContainsExpectedValues(task);
  }

  // Test 7
  @Test(expected = UnknownErrorException.class)
  public void throwsUnknownErrorExceptionIfThereIsNotHandledErrorGettingTaskById()
          throws Exception {
      enqueueMockResponse(418);

      apiClient.getTaskById(ANY_TASK_ID);
  }

  // Test 8
  @Test(expected = ItemNotFoundException.class)
  public void returnsItemNotFoundGettingTaskByIdIfThereIsNoTaskWithThePassedId() throws Exception {
      enqueueMockResponse(404);

      apiClient.getTaskById(ANY_TASK_ID);
  }

  // Test 9
  @Test
  public void sendsAcceptAndContentTypeHeadersToFindById() throws Exception {
      enqueueMockResponse();

      apiClient.getTaskById(ANY_TASK_ID);

      assertRequestContainsHeader("Accept", "application/json");
  }

  /*** TESTS FOR ADD TASK ***/

  // Test 10
  @Test
  public void sendsAddTaskRequestToTheCorrectPath() throws Exception {
      enqueueMockResponse();

      apiClient.addTask(ANY_TASK);

      assertPostRequestSentTo("/todos");
  }

  // Test 11
  @Test
  public void sendsTheCorrectBodyAddingANewTask() throws Exception {
      enqueueMockResponse();

      apiClient.addTask(ANY_TASK);

      assertRequestBodyEquals("addTaskRequest.json");
  }

  // Test 12
  @Test
  public void testParsesTheTaskCreatedProperlyAddingANewTask() throws Exception {
      enqueueMockResponse(201, "addTaskResponse.json");

      TaskDto task = apiClient.addTask(ANY_TASK);

      assertTaskContainsExpectedValues(task);
  }

  // Test 13
  @Test(expected = UnknownErrorException.class)
  public void returnsUnknownErrorIfThereIsAnyErrorAddingATask() throws Exception {
      enqueueMockResponse(418);

      apiClient.addTask(ANY_TASK);
  }

  private void assertTaskContainsExpectedValues(TaskDto task) {
      assertEquals(task.getId(), "1");
      assertEquals(task.getUserId(), "1");
      assertEquals(task.getTitle(), "delectus aut autem");
      assertFalse(task.isFinished());
  }
}
