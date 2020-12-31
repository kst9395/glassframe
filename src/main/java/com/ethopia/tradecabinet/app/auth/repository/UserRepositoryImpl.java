package com.ethopia.tradecabinet.app.auth.repository;

import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.app.common.ApiException;
import com.ethopia.tradecabinet.app.common.Errors;
import com.ethopia.tradecabinet.model.AppUser;
import com.ethopia.tradecabinet.model.AppUserGroup;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import io.vertx.reactivex.core.WorkerExecutor;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;

import java.util.List;
import java.util.function.Supplier;

public class UserRepositoryImpl implements UserRepository {

    private ServerRuntime serverRuntime;
    private WorkerExecutor workerExecutor;

    @Inject
    public void setWorkerExecutor(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor;
    }

    @Inject
    public void setServerRuntime(ServerRuntime serverRuntime) {
        this.serverRuntime = serverRuntime;
    }

    private <T> Maybe<T> async(Supplier<T> blocking) {
        return workerExecutor.rxExecuteBlocking(promise -> {
            try {
                promise.complete(blocking.get());
            } catch (Exception e) {
                promise.fail(e);
            }
        }, false);
    }

    @Override
    public Maybe<Boolean> existsByUsername(String username) {
        return async(() -> _existsByUsername(username));

    }

    @Override
    public Maybe<Boolean> existsByEmail(String email) {
        return async(() -> _existsByEmail(email));
    }

    private Boolean _existsByEmail(String email) {
        ObjectContext ctx = serverRuntime.newContext();
        AppUser appUser = ObjectSelect.query(AppUser.class).where(AppUser.EMAIL.eq(email)).selectFirst(ctx);
        return appUser != null;
    }

    private Boolean _existsByUsername(String username) {
        ObjectContext ctx = serverRuntime.newContext();
        AppUser appUser = ObjectSelect.query(AppUser.class).where(AppUser.USERNAME.eq(username)).selectFirst(ctx);
        return appUser != null;
    }

    @Override
    public Maybe<AppUser> createAppUser(RegisterUserInput userInput, String hashed) {
        return async(() -> _createAppUser(userInput, hashed));
    }


    @Override
    public Maybe<AppUser> retrieveAppUserByUsername(String username) {
        return async(() -> _retrieveAppUserByUsername(username));
    }

    @Override
    public Maybe<List<AppUser>> retrieveAppUserWithoutGroup() {
        return async(() -> {
            ObjectContext ctx = serverRuntime.newContext();
            return ObjectSelect.query(AppUser.class)
                    .where(AppUser.APP_USER_GROUPS.countDistinct().eq(0L))
                    .select(ctx);
        });
    }
    
    private AppUser _retrieveAppUserByUsername(String username) {
        ObjectContext ctx = serverRuntime.newContext();
        AppUser appUser = ObjectSelect.query(AppUser.class, AppUser.USERNAME.eq(username))
                .prefetch(AppUser.APP_USER_ROLES.joint())
                .prefetch(AppUser.APP_USER_GROUPS.joint())
                .selectOne(ctx);

        if (appUser == null) {
            throw new ApiException(Errors.API_USER_MANAGEMENT_USER_NOT_FOUND_ERROR);
        }
        return appUser;
    }

    private AppUser _createAppUser(RegisterUserInput userInput, String hashed) {
        ObjectContext ctx = serverRuntime.newContext();
        try {
            AppUser appUser = ctx.newObject(AppUser.class);
            appUser.populate(userInput, hashed);
            AppUserGroup appUserGroup = ObjectSelect.query(AppUserGroup.class).where(AppUserGroup.GROUP_NAME.eq(AppUserGroup.DEFAULT_GROUP_NAME)).selectOne(ctx);
            appUser.addToAppUserGroups(appUserGroup);

            ctx.commitChanges();
            return appUser;
        } catch (Exception e) {
            ctx.rollbackChanges();
            throw e;
        }
    }
}
