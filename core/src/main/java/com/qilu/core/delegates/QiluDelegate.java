package com.qilu.core.delegates;

public abstract class QiluDelegate extends PermissionCheckerDelegate {

    @SuppressWarnings("unchecked")
    public <T extends QiluDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }
}
