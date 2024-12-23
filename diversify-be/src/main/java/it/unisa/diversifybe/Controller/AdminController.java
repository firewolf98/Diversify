package it.unisa.diversifybe.Controller;

import it.unisa.diversifybe.Model.CampagnaCrowdFunding;
import it.unisa.diversifybe.Model.Forum;
import it.unisa.diversifybe.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/ban-user/{username}")
    public ResponseEntity<Void> banUser(@PathVariable String username) {
        adminService.banUser(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forums")
    public ResponseEntity<Forum> createForum(@RequestBody Forum forum, @RequestParam boolean ruolo) {
        Forum createdForum = adminService.createForumAsAdmin(forum, ruolo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForum);
    }

    @PutMapping("/forums/{id}")
    public ResponseEntity<Forum> updateForum(@PathVariable String id, @RequestBody Forum updatedForum, @RequestParam boolean ruolo) {
        Forum forum = adminService.updateForumAsAdmin(id, updatedForum, ruolo);
        return ResponseEntity.ok(forum);
    }

    @PostMapping("/campaigns")
    public ResponseEntity<CampagnaCrowdFunding> createCampaign(@RequestBody CampagnaCrowdFunding campagna, @RequestParam boolean ruolo) {
        CampagnaCrowdFunding createdCampaign = adminService.createCampaignAsAdmin(campagna, ruolo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/campaigns/{id}")
    public ResponseEntity<CampagnaCrowdFunding> updateCampaign(@PathVariable String id, @RequestBody CampagnaCrowdFunding updatedCampagna, @RequestParam boolean ruolo) {
        CampagnaCrowdFunding campaign = adminService.updateCampaignAsAdmin(id, updatedCampagna, ruolo);
        return ResponseEntity.ok(campaign);
    }
}
