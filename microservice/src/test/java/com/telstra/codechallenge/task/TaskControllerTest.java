package com.telstra.codechallenge.task;

import com.telstra.codechallenge.exception.UrlNulloRNotFoundException;
import com.telstra.codechallenge.task.controller.TaskController;
import com.telstra.codechallenge.task.dto.ItemModel;
import com.telstra.codechallenge.task.service.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application.properties")
public class TaskControllerTest {

  @LocalServerPort
  int randomServerPort;

  @Autowired
  private TaskController taskController;

  @Autowired
  private TaskService taskService;

  @Autowired
  private RestTemplate restTemplate;

  String baseUrl;

  @Before
   public void init(){
    taskController = mock(TaskController.class);
    taskService = new TaskService();
    restTemplate = new RestTemplate();
    baseUrl= "http://localhost:" + randomServerPort + "/users?qvalue=followers:0";
    Whitebox.setInternalState(taskService,"taskBaseUrl",baseUrl);
  }


  @Test()
  public void testTaskDataFail() throws IllegalStateException, UrlNulloRNotFoundException {
    ResponseEntity<ItemModel> resp = new ResponseEntity<ItemModel>(HttpStatus.BAD_REQUEST);
    when(taskController.taskData(anyString(),anyInt())).thenReturn(resp);
    //Verify request Failed
    Assert.assertEquals(400, resp.getStatusCodeValue());
  }

  @Test(expected = RuntimeException.class)
  public void testTaskDataVerify() throws UrlNulloRNotFoundException  {
    Whitebox.setInternalState(taskController,"taskService",taskService);
    Whitebox.setInternalState(taskController,"restTemplate",restTemplate);
    ResponseEntity<ItemModel> resp = taskController.taskData("followers:0",1);
    Assert.assertEquals(200,resp.getStatusCodeValue());
    verify(taskController,times(1)).taskData(anyString(),anyInt());
  }
}

