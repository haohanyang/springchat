package haohanyang.springchat.server.controllers;

import haohanyang.springchat.common.ChatNotification;
import haohanyang.springchat.common.ChatNotificationType;

import haohanyang.springchat.server.services.MessageService;
import haohanyang.springchat.server.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserGroupController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserGroupService userGroupService;
    private final MessageService messageService;

    @Autowired
    public UserGroupController(SimpMessagingTemplate simpMessagingTemplate, UserGroupService userGroupService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userGroupService = userGroupService;
        this.messageService = new MessageService(simpMessagingTemplate);
    }

    @PutMapping("/join")
    public ResponseEntity<ChatNotification> joinGroup(@RequestBody String groupId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = (String) authentication.getPrincipal();
        var result = userGroupService.addMember(username, groupId);
        if (result.type() == ChatNotificationType.SUCCESS) {
            // Notify group members
            messageService.sendGroupNotification(groupId, result);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
