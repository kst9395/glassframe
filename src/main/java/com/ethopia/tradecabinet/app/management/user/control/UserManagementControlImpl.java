package com.ethopia.tradecabinet.app.management.user.control;

import com.ethopia.tradecabinet.app.auth.repository.UserRepository;
import com.ethopia.tradecabinet.app.management.user.dto.UserDetailResponse;
import com.ethopia.tradecabinet.app.management.user.dto.UserListResponse;
import com.ethopia.tradecabinet.app.management.user.dto.UserListResponseItem;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserManagementControlImpl implements UserManagementControl {


    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementControlImpl.class.getName());

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<UserDetailResponse> userDetailByUsername(String username) {
        return userRepository.retrieveAppUserByUsername(username).map(UserDetailResponse::from);
    }

    @Override
    public Maybe<UserListResponse> listUsers(int page, int pageSize) {
        logger.info("[UserManagementControlImpl][listUsers] - " + page + "," + pageSize);
        return userRepository.countTotalAppUsers()
                .flatMap(totalAppUser -> {
                    logger.info("total app user: " + totalAppUser);

                    UserListResponse response = new UserListResponse();

                    int totalPage = new Double(Math.round(totalAppUser / pageSize)).intValue() + 1;

                    response.setTotalPage(totalPage);
                    //cap the maximum page
                    int targetPage = Math.min(page, totalPage);
                    response.setPage(targetPage);
                    return userRepository.paginateAppUser(targetPage, pageSize)
                            .flatMap(appUsers -> {
                                List<UserListResponseItem> data = appUsers.stream().map(UserListResponseItem::from).collect(Collectors.toList());
                                response.setData(data);
                                return Maybe.just(response);
                            });

                });
    }
}
