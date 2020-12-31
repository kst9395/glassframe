package com.ethopia.tradecabinet.app.management.user.control;

import com.ethopia.tradecabinet.app.management.user.dto.UserDetailResponse;
import io.reactivex.Maybe;

public interface UserManagementControl {
    Maybe<UserDetailResponse> userDetailByUsername(String username);

}
