package haohanyang.springchat.controllers;


import haohanyang.springchat.dtos.NotificationDTO;
import haohanyang.springchat.dtos.NotificationType;
import haohanyang.springchat.services.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserGroupController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupController(SimpMessagingTemplate simpMessagingTemplate, UserGroupService userGroupService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userGroupService = userGroupService;
    }

    @PutMapping("/api/join")
    public ResponseEntity<String> joinGroup(@RequestBody String groupName,
            @RequestParam(defaultValue = "false") boolean initUser) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            var username = (String) authentication.getPrincipal();
            if (initUser) {
                // for test, mock stomp subscribe
                userGroupService.addUser(username);
            }
            userGroupService.addMember(username, groupName);
            // Notify group members
            var notification = new NotificationDTO(NotificationType.INFO,
                    "u/" + username + " joined g/" + groupName);
            simpMessagingTemplate.convertAndSend("/notify/group/" + groupName, notification);
            return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
