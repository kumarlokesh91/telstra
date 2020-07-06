package com.telstra.codechallenge.task;

import com.telstra.codechallenge.exception.UrlNulloRNotFoundException;
import com.telstra.codechallenge.task.dto.ItemModel;
import com.telstra.codechallenge.task.service.TaskService;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


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

  @Test(expected= UrlNulloRNotFoundException.class)
  public void givenMockingIsDoneByMock_whenGetIsCalled_thenThrowUrlNulloRNotFoundException() throws UrlNulloRNotFoundException {
    ItemModel mit=getMockData();
    ItemModel items=taskService.getTaskData("followers:0",3);
    Assert.assertEquals(mit.getItems().size(), items.getItems().size());
    Assert.assertNotNull(items.getItems());
    verify(taskService,times(1)).getTaskData("followers:0",3);
  }

  @Test(expected=ResourceAccessException.class)
  public void givenMockingIsDoneByMock_whenGetIsCalled_thenReturnSuccess() throws UrlNulloRNotFoundException {
    ItemModel mit=getMockData();
    TaskService task = mock(TaskService.class);
    Whitebox.setInternalState(task,"restTemplate",restTemplate);
    baseUrl="http://localhost:" + randomServerPort + "/users?qvalue=followers:0";
    when(task.getTaskBaseUrl()).thenReturn(baseUrl);
    when(restTemplate.getForObject(baseUrl,ItemModel.class)).thenReturn(mit);
    when(task.getTaskData("followers:0",3)).thenReturn(new ItemModel());
    verify(task,times(1)).getTaskData("followers:0",3);
    verify(restTemplate,times(1)).getForObject(baseUrl,ItemModel.class);
  }

  @Test
  public void testsetLimitToItem(){
    ItemModel mit=getMockData();
    ItemModel it=taskService.setLimitToItem(mit,1);
    Assert.assertNotNull(it);

  }
  private ItemModel getMockData() {
    ItemModel mit =new ItemModel();
    List<ItemModel.Items> lis =new ArrayList<>();
    ItemModel.Items item1=new ItemModel.Items();
    item1.setId(13064110L);
    item1.setLogin("mercuryphp");
    item1.setHtmlUrl("https://github.com/mercuryphp");
    ItemModel.Items item2=new ItemModel.Items();
    item2.setId(13064136L);
    item2.setHtmlUrl("html_url");
    item2.setLogin("https://github.com/devgarvit");
    ItemModel.Items item3=new ItemModel.Items();
    item3.setId(13064166L);
    item3.setHtmlUrl("https://github.com/FreeSpeak");
    item3.setLogin("FreeSpeak");
    lis.add(item1);
    lis.add(item2);
    lis.add(item3);
    mit.setItems(lis);
    return mit;
  }
}











