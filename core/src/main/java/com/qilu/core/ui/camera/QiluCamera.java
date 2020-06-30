package com.qilu.core.ui.camera;

import android.net.Uri;

import com.qilu.core.delegates.PermissionCheckerDelegate;
import com.qilu.core.util.file.FileUtil;

//照相机调用类
public class QiluCamera {

    public static Uri createCropFile() {
        return Uri.parse
                (FileUtil.createFile("MagicMirror",
                        FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(PermissionCheckerDelegate delegate) {
        new CameraHandler(delegate).beginCameraDialog();
    }
}
