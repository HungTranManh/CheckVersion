package checkversion.solar.com.checkversion.service;

import android.os.Binder;

public class MyBirder extends Binder {
    private VersionService versionService;

    public MyBirder(VersionService versionService) {
        this.versionService = versionService;
    }

    public VersionService getVersionService() {
        return versionService;
    }
}
