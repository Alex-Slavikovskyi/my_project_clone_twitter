package app.service;

import app.exceptions.userError.IncorrectUserIdException;
import app.exceptions.userError.UserNotFoundException;
import app.model.Chat;
import app.model.Message;
import app.model.UserModel;
import app.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserModelService extends GeneralService<UserModel> {

  private final UserModelRepository userModelRepository;

  /**
   * Methods returns Optional of UserModel by different parameters
   */
  public Optional<UserModel> getUser(String email) {
    return this.userModelRepository.findByEmail(email);
  }

  public Optional<UserModel> getUser(Long id) {
    return this.userModelRepository.findById(id);
  }

  public Optional<UserModel> getUserByToken(String refreshToken) {
    return this.userModelRepository.findByToken(refreshToken);
  }

  @Transactional
  public void subscribe(Long userCurrentId, Long userToFollowingId) {
    if (userToFollowingId == null || Objects.equals(userCurrentId, userToFollowingId))
      throw new IncorrectUserIdException(userToFollowingId);
    UserModel userCurrent = this.getUser(userCurrentId).orElseThrow(() -> new UserNotFoundException(userCurrentId));
    UserModel userToFollowing = this.getUser(userToFollowingId).orElseThrow(() -> new UserNotFoundException(userToFollowingId));
    userCurrent.getFollowings().add(userToFollowing);
  }

  @Transactional
  public void unsubscribe(Long userCurrentId, Long userToUnFollowingId) {
    UserModel userCurrent = this.getUser(userCurrentId).orElseThrow(() -> new UserNotFoundException(userCurrentId));
    UserModel userToUnFollowing = this.getUser(userToUnFollowingId).orElseThrow(() -> new UserNotFoundException(userToUnFollowingId));
    userCurrent.getFollowings().remove(userToUnFollowing);
  }


  /**
   * Method returns true if provided email address is present in DB
   */
  public boolean checkEmail(String email) {
    return this.userModelRepository.findByEmail(email).isPresent();
  }

}
