package app.controller;

import app.annotations.Marker;
import app.dto.rq.UserModelRequest;
import app.dto.rs.UserModelResponse;
import app.facade.UserModelFacade;
import app.service.UserModelService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Log4j2
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserModelService userModelService;
  private final UserModelFacade userModelFacade;

  @GetMapping("{userId}")
  public ResponseEntity<UserModelResponse> getUserById(@PathVariable(name = "userId") @Positive Long userId) {
    return ResponseEntity.ok(userModelFacade.getUserById(userId));
  }

  @GetMapping("profile")
  public ResponseEntity<UserModelResponse> getUser(HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.getUserById((Long) httpRequest.getAttribute("userId")));
  }

  @Validated({Marker.Update.class})
  @PutMapping("profile")
  public ResponseEntity<UserModelResponse> updateUser(@RequestBody @JsonView({Marker.Update.class}) @Valid UserModelRequest userRequestDTO, HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.updateUser((Long) httpRequest.getAttribute("userId"), userRequestDTO));
  }

  @PutMapping("profile/avatar_img")
  public ResponseEntity<UserModelResponse> uploadAvatarImg(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.convertToDto(
      userModelService.uploadAvatarImg((Long) httpRequest.getAttribute("userId"), file)));
  }

  @PutMapping("profile/header_img")
  public ResponseEntity<UserModelResponse> uploadHeaderImg(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.convertToDto(
      userModelService.uploadHeaderImg((Long) httpRequest.getAttribute("userId"), file)));
  }


  @PostMapping("subscribe/{userIdToFollowing}")
  public ResponseEntity<UserModelResponse> subscribe(@PathVariable(name = "userIdToFollowing") @Positive Long userIdToFollowing, HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.convertToDto(
      userModelService.subscribe((Long) httpRequest.getAttribute("userId"), userIdToFollowing)
    ));
  }

  @PostMapping("unsubscribe/{userIdToUnFollowing}")
  public ResponseEntity<UserModelResponse> unsubscribe(@PathVariable(name = "userIdToUnFollowing") @Positive Long userIdToUnFollowing, HttpServletRequest httpRequest) {
    return ResponseEntity.ok(userModelFacade.convertToDto(
      userModelService.unsubscribe((Long) httpRequest.getAttribute("userId"), userIdToUnFollowing)
    ));
  }

}
