package com.telstra.codechallenge.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

  @Autowired
  private TaskService taskService;

  @RequestMapping(path = "/users", method = RequestMethod.GET)
  public   @ResponseBody ItemModel  taskData(@RequestParam("q") String q) {
    System.out.println("q value" + q);
   ItemModel itemModel = taskService.getTaskData(q);
    System.out.println("item "+itemModel.getItem().size());
    return itemModel;
  }

}
