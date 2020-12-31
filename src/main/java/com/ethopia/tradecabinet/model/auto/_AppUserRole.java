package com.ethopia.tradecabinet.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import com.ethopia.tradecabinet.model.AppRole;
import com.ethopia.tradecabinet.model.AppUser;

/**
 * Class _AppUserRole was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _AppUserRole extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<AppRole> ROLE = Property.create("role", AppRole.class);
    public static final Property<AppUser> USER = Property.create("user", AppUser.class);


    protected Object role;
    protected Object user;

    public void setRole(AppRole role) {
        setToOneTarget("role", role, true);
    }

    public AppRole getRole() {
        return (AppRole)readProperty("role");
    }

    public void setUser(AppUser user) {
        setToOneTarget("user", user, true);
    }

    public AppUser getUser() {
        return (AppUser)readProperty("user");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "role":
                return this.role;
            case "user":
                return this.user;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "role":
                this.role = val;
                break;
            case "user":
                this.user = val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.role);
        out.writeObject(this.user);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.role = in.readObject();
        this.user = in.readObject();
    }

}