package app.controller;

import app.dto.rq.TweetRequestDTO;
import app.dto.rs.TweetResponseDTO;
import app.facade.TweetFacade;
import app.model.AttachmentImage;
import app.model.Tweet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.stream.Collectors;

@Log4j2
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tweet")
public class TweetController {

  private final TweetFacade tweetFacade;

  //@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PutMapping
  public ResponseEntity<TweetResponseDTO> createNewTweet(@ModelAttribute @Valid TweetRequestDTO requestDTO,
                                                         HttpServletRequest httpRequest){
    return ResponseEntity.ok(tweetFacade.createTweet((Long) httpRequest.getAttribute("userId"), requestDTO));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TweetResponseDTO> getTweetById(@PathVariable(name = "id") @Positive Long tweetId){
    return ResponseEntity.ok(tweetFacade.getTweetById(tweetId));
  }
}
