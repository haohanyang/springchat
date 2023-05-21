package haohanyang.springchat.controllers.apis;

import haohanyang.springchat.dtos.GroupDto;
import haohanyang.springchat.identity.ApplicationUserDetails;
import haohanyang.springchat.services.GroupService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
public class GroupController {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(GroupController.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GroupService groupService;

    @Autowired
    public GroupController(SimpMessagingTemplate simpMessagingTemplate, GroupService groupService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.groupService = groupService;
    }

    @GetMapping("/api/groups")
    public Set<GroupDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/api/groups/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Integer groupId) {
        try {
            var group = groupService.getGroup(groupId);
            return ResponseEntity.ok().body(group);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unknown error occurred");
        }
    }

    @PostMapping("/api/groups")
    public ResponseEntity<?> createGroup(@AuthenticationPrincipal ApplicationUserDetails user, @RequestBody GroupDto groupDto) {
        try {
            var username = user.getUsername();
            if (!Objects.equals(username, groupDto.creator().username()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only create group for yourself");

            var id = groupService.createGroup(username, groupDto.name());
            return ResponseEntity.ok().body(id);
        } catch (Exception e) {
            logger.error("Failed to create group {} with unexpected error {}", groupDto.name(), e.getMessage());
            return ResponseEntity.status(500).body("Unknown error occurred");
        }
    }
}
