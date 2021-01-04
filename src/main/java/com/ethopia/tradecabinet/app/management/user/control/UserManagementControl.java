package com.ethopia.tradecabinet.app.management.user.control;

import com.ethopia.tradecabinet.app.management.user.dto.UserDetailResponse;
import com.ethopia.tradecabinet.app.management.user.dto.UserListResponse;
import io.reactivex.Maybe;

public interface UserManagementControl {
    Maybe<UserDetailResponse> userDetailByUsername(String username);

    Maybe<UserListResponse> listUsers(int page, int pageSize);


}
