package com.telstra.codechallenge.task;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@Service
public class TaskService {

//  private static final Logger log = (Logger) LoggerFactory.getLogger(TaskService.class);
  public String getTaskBaseUrl() {
    return taskBaseUrl;
  }

  @Value("${github.base.url}")
  private String taskBaseUrl;

  @Autowired
  private RestTemplate restTemplate;

  public ItemModel getTaskData(String fs) {

    System.out.println("fs "+getTaskBaseUrl()+fs);
    ItemModel item =restTemplate.getForObject(taskBaseUrl + fs, ItemModel.class);
//    log.info("item.getItem().size()"+item.getItem().size());
    System.out.println("itemlist "+ item.getItem().size());
    return item;

  }
}