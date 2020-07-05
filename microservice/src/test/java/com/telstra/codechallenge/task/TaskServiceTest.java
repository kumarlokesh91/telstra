package com.telstra.codechallenge.task;

import com.telstra.codechallenge.exception.UrlNulloRNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application.properties")
public class TaskServiceTest {

  @LocalServerPort
  int randomServerPort;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private TaskService taskService;

  String baseUrl;

  @Before
  public void init() {
    taskService = new TaskService();
    restTemplate = new RestTemplate();
  }

  @Test
  public void shouldApplicationProperty_overridePropertyValues() {
    baseUrl = "http://localhost:" + randomServerPort + "/users?qvalue=followers:0";
    Whitebox.setInternalState(taskService, "taskBaseUrl", baseUrl);
    String firstProperty = taskService.getTaskBaseUrl();
    assertEquals("http://localhost:" + randomServerPort + "/users?qvalue=followers:0", firstProperty);
  }

  @Test()
  public void testForTakDataFailthrowsUrlNulloRNotFoundException() throws UrlNulloRNotFoundException {
    taskService =mock(TaskService.class);
    Whitebox.setInternalState();
    when(taskService.getTaskData(anyString(),anyInt())).thenThrow(new
        UrlNulloRNotFoundException("Url is empty or not proper"));
    Assert.assertNull(taskService.getTaskBaseUrl());
  }

  @Test(expected= ResourceAccessException.class)
  public void givenMockingIsDoneByMock_whenGetIsCalled_thenReturnsObject() throws UrlNulloRNotFoundException {
    ItemModel mitem = new ItemModel();
    when(restTemplate.getForObject(
     "http://localhost:8080/users?qvalue=followers:0&limit=3",ItemModel.class))
        .thenReturn(mitem);
    ItemModel itemModel =taskService.getTaskData("followers:0",3);
    Assert.assertEquals(mitem.getItems().get(0).getId(),itemModel.getItems().get(0).getId());
    Assert.assertEquals(mitem.getItems().size(), itemModel.getItems().size());
  }
}











