package com.telstra.codechallenge.task;


import com.telstra.codechallenge.exception.UrlNulloRNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

  @Autowired
  private TaskService taskService;

 @GetMapping("/users")
  public ResponseEntity<ItemModel> taskData(@RequestParam("qvalue") String qValue,
                                         @RequestParam("limit") int limit)
                                         throws UrlNulloRNotFoundException {
     ItemModel items=taskService.getTaskData(qValue,limit);
   return ResponseEntity.ok().body(items);
  }

}
