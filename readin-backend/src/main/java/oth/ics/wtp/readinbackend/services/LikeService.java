package oth.ics.wtp.readinbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.readinbackend.ClientErrors;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.entities.Like;
import oth.ics.wtp.readinbackend.entities.Post;
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
        if (likeRepository.existsByAppUser_IdAndPost_Id(appUserId, postId)) {
            throw ClientErrors.userAlreadyLikedPost(appUserId,postId);
        }

            AppUser appUser = appUserRepository.findById(appUserId).orElseThrow(()-> ClientErrors.userIdNotFound(appUserId));
            Post post = postRepository.findById(postId).orElseThrow(()-> ClientErrors.postNotFound(postId));
            Like like = toEntity(appUser,post);
            likeRepository.save(like);

    }

    private Like toEntity( AppUser appUser, Post post) {
      return new Like(appUser,post);
    }
    @Transactional
    public void unlikePost(long appUserId, Long postId) {
        likeRepository.deleteByAppUser_IdAndPost_Id(appUserId, postId);
    }
    public boolean hasUserLikedPost(long  appUserId, Long postId) {
        return likeRepository.existsByAppUser_IdAndPost_Id(appUserId, postId);
    }
    public int getLikeCount(Long postId) {
      return likeRepository.countByPost_Id(postId);
    }








}
