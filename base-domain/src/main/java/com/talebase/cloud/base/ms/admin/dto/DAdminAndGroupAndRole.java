package com.talebase.cloud.base.ms.admin.dto;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;

/**
 * Created by daorong.li on 2016-12-2.
 */
public class DAdminAndGroupAndRole {
    private DAdmin admin;
    private DGroupAndRole dGroupAndRole;

    public DAdminAndGroupAndRole(DAdmin admin,DGroupAndRole dGroupAndRole){
        this.dGroupAndRole = dGroupAndRole;
        this.admin = admin;
    }

    public DAdmin gettAdmin() {
        return admin;
    }

    public void settAdmin(DAdmin admin) {
        this.admin = admin;
    }

    public DGroupAndRole getdGroupAndRole() {
        return dGroupAndRole;
    }

    public void setdGroupAndRole(DGroupAndRole dGroupAndRole) {
        this.dGroupAndRole = dGroupAndRole;
    }
}
