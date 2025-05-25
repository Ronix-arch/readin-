package oth.ics.wtp.readinbackend;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ClientErrors {
    private static final Logger logger = LoggerFactory.getLogger(ClientErrors.class);

    public static ResponseStatusException unauthorized() {
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "valid Basic credentials required"));
    }

    public static ResponseStatusException userNotFound(String name) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "user with name " + name));
    }
    public static ResponseStatusException userNameTaken(String name) {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "user name already taken: " + name));
    }

    public static ResponseStatusException invalidCredentials() {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));
    }

    private static ResponseStatusException log(ResponseStatusException e) {
        logger.error(ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
        return e;
    }

    public static ResponseStatusException userIdNotFound(long userId) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + userId));
    }

    public static ResponseStatusException postNotFound(long postId) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "post with id " + postId));
    }

    public static ResponseStatusException followingAlreadyExsists(long followerId, long followeeId) {
        return log(new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "the Following relationship between followerid  " + followerId + " and followeid " + followeeId+" already exists" ));
    }

    public static ResponseStatusException followingDoesNotExsists(long followerId, long followeeId) {
        return log(new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "the Following relationship between followerid  " + followerId + " and followeid " + followeeId+" does not exists" ));
    }
}
