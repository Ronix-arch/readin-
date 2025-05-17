package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.entities.Like;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.repositories.LikeRepository;
import oth.ics.wtp.readinbackend.repositories.PostRepository;

@Service public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;

  @Autowired public LikeService(LikeRepository likeRepository, PostRepository postRepository, AppUserRepository appUserRepository) {
      this.likeRepository = likeRepository;
      this.postRepository = postRepository;
      this.appUserRepository = appUserRepository;

  }
    public void likePost( long appUserId, Long postId) {
        if (!likeRepository.existsByAppUserIdAndPostId(appUserId, postId)) {
            Like like = new Like();
            like.setUser(appUserRepository.findById(appUserId).orElseThrow(()-> ClientErrors.userIdNotFound(appUserId)));
            like.setPost(postRepository.findById(postId).orElseThrow(()-> ClientErrors.postNotFound(postId)));
            likeRepository.save(like);
        }
    }
    public void unlikePost(long appUserId, Long postId) {
        likeRepository.deleteByAppUserIdAndPostId(appUserId, postId);
    }
    public boolean hasUserLikedPost(long  appUserId, Long postId) {
        return likeRepository.existsByAppUserIdAndPostId(appUserId, postId);
    }
    public int getLikeCount(Long postId) {
      return likeRepository.countByPostId(postId);
    }








}
