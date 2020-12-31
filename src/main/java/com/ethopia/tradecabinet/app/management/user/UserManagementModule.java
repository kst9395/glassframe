package com.ethopia.tradecabinet.app.management.user;

import com.ethopia.tradecabinet.app.management.user.control.UserManagementControl;
import com.ethopia.tradecabinet.app.management.user.control.UserManagementControlImpl;
import com.google.inject.AbstractModule;

public class UserManagementModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserManagementControl.class).to(UserManagementControlImpl.class);
    }
}
