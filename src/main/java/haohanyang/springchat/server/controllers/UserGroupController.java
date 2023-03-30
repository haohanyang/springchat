package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.ChatNotification;
import haohanyang.springchat.common.ChatNotificationType;

import haohanyang.springchat.server.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserGroupController {
    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PutMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody String groupId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = (String) authentication.getPrincipal();
        var result = userGroupService.addMember(username, groupId);
        if (result.type() == ChatNotificationType.SUCCESS) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.message());
        }
    }

}
