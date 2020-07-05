package com.telstra.codechallenge.task;

import com.telstra.codechallenge.exception.UrlNulloRNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Component
public class TaskService {

  TaskService() {
    // for sonar
  }
  @Value("${github.base.url}")
  private String taskBaseUrl;

  @Autowired
  private RestTemplate restTemplate;

  public String getTaskBaseUrl() {
    return taskBaseUrl;
  }

  public ItemModel getTaskData(String fs,int limit) throws  UrlNulloRNotFoundException {
    if(StringUtils.isEmpty(taskBaseUrl)){
      throw new UrlNulloRNotFoundException("Url is empty/not proper");
    }
    ItemModel item =restTemplate.getForObject(taskBaseUrl + fs, ItemModel.class);
     return setLimitToItem(item,limit);
  }

  public ItemModel setLimitToItem(ItemModel item,int limit) {
	ItemModel itemL =new ItemModel();
	itemL.setItems(item.getItems().stream().limit(limit).collect(Collectors.toList()));
	return itemL;
  }
}