package custom.frame.permission;

import android.support.annotation.NonNull;

public interface OnActivityPermissionCallback
{

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults);

    void onActivityForResult(int requestCode);
}
