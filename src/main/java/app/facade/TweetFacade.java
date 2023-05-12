package app.facade;

import app.dto.rq.TweetRequest;
import app.dto.rs.TweetResponse;
import app.dto.rs.UserModelResponse;
import app.model.Tweet;
import app.model.UserModel;

import javax.annotation.PostConstruct;

public class TweetFacade extends GeneralFacade<Tweet, TweetRequest, TweetResponse> {
  @PostConstruct
  public void init() {
    super.getMm().typeMap(Tweet.class, TweetResponse.class)
      .addMapping(src -> src.getBody(), TweetResponse::setBody)
      .addMapping(src -> src.getAttachmentImages(), TweetResponse::setAttachments)
      .addMapping(src -> src.getUser().getUserTag(), TweetResponse::setUserTag)
      .addMapping(src -> src.getUser().getAvatarImgUrl(), TweetResponse::setUserAvatarImage)
      .addMapping(src -> src.getCountLikes(), TweetResponse::setCountLikes)
      .addMapping(src -> src.getParentTweetId(), TweetResponse::setParentTweet);
  }
}
